/*******************************************************************************
 * Copyright (C) 2007, Robin Rosenberg <robin.rosenberg@dewire.com>
 * Copyright (C) 2010, Jens Baumgart <jens.baumgart@sap.com>
 * Copyright (C) 2010, Mathias Kinzler <mathias.kinzler@sap.com>
 * Copyright (C) 2012, François Rey <eclipse.org_@_francois_._rey_._name>
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 *******************************************************************************/
package org.jboss.tools.openshift.egit.internal.test.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ICoreRunnable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.egit.core.project.RepositoryMapping;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.FileUtils;

public class TestUtils {

	public final static String AUTHOR = "The Author <The.author@some.com>";

	public final static String COMMITTER = "The Commiter <The.committer@some.com>";

	private static final int MAX_DELETE_RETRY = 5;

	private static final int DELETE_RETRY_DELAY = 1000; // ms

	public TestUtils() {
	}
	
	/**
	 * Create a "temporary" directory
	 * 
	 * @param name
	 *            the name of the directory
	 * @return a directory as child of a "temporary" folder in the user home
	 *         directory; may or may not exist
	 * @throws IOException
	 */
	public File createTempDir(String name) throws IOException {
		File userHome = FS.DETECTED.userHome();
		File rootDir = new File(userHome, "EGitCoreTestTempDir");
		File result = new File(rootDir, name);
		if (result.exists())
			FileUtils.delete(result, FileUtils.RECURSIVE | FileUtils.RETRY);
		return result;
	}

	public static File createGitDir(TestProject testProject) {
		return new File(testProject.getProject().getLocation().toFile(), Constants.DOT_GIT);
	}

	/**
	 * Cleanup: delete the "temporary" folder and all children
	 * 
	 * @throws IOException
	 */
	public void deleteTempDirs() throws IOException {
		File userHome = FS.DETECTED.userHome();
		File rootDir = new File(userHome, "EGitCoreTestTempDir");
		if (rootDir.exists())
			FileUtils.delete(rootDir, FileUtils.RECURSIVE | FileUtils.RETRY);
	}
	
	/**
	 * Produce a simple directory listing.
	 *
	 * @param directory
	 *            to list
	 * @param recursive
	 *            whether to descend into sub-directories
	 */
	public static void listDirectory(File directory, boolean recursive) {
		try {
			java.nio.file.Path top = directory.toPath();
			Files.walkFileTree(top,
				new SimpleFileVisitor<java.nio.file.Path>() {

					private void print(java.nio.file.Path path,
							BasicFileAttributes attrs) {
						StringBuilder b = new StringBuilder();
						b.append(attrs.lastModifiedTime().toString());
						b.append(' ');
						b.append(path.toString());
						if (attrs.isSymbolicLink()) {
							b.append(" (symlink)");
						} else if (attrs.isDirectory()) {
							b.append('/');
						}
						System.out.println(b.toString());
					}

					@Override
					public FileVisitResult preVisitDirectory(
							java.nio.file.Path dir,
							BasicFileAttributes attrs) throws IOException {
						print(dir, attrs);
						return (recursive || top.equals(dir))
								? FileVisitResult.CONTINUE
								: FileVisitResult.SKIP_SUBTREE;
					}

					@Override
					public FileVisitResult visitFile(
							java.nio.file.Path file,
							BasicFileAttributes attrs) throws IOException {
						print(file, attrs);
						return FileVisitResult.CONTINUE;
					}
				});
		} catch (Exception e) {
			System.err.println("[ERROR] Error listing directory: " + directory);
			e.printStackTrace();
		}
	}

	/**
	 * Add a file to an existing project
	 * 
	 * @param project
	 *            the project
	 * @param path
	 *            e.g. "folder1/folder2/test.txt"
	 * @param content
	 *            the contents
	 * @return the file
	 * @throws Exception
	 *             if the file can not be created
	 */
	public IFile addFileToProject(IProject project, String path, String content) throws Exception {
		IPath filePath = new Path(path);
		IFolder folder = null;
		for (int i = 0; i < filePath.segmentCount() - 1; i++) {
			if (folder == null) {
				folder = project.getFolder(filePath.segment(i));
			} else {
				folder = folder.getFolder(filePath.segment(i));
			}
			if (!folder.exists())
				folder.create(false, true, null);
		}
		IFile file = project.getFile(filePath);
		file.create(new ByteArrayInputStream(content.getBytes(project.getDefaultCharset())), true, null);
		return file;
	}

	/**
	 * Change the content of a file
	 * 
	 * @param project
	 * @param file
	 * @param newContent
	 * @return the file
	 * @throws Exception
	 */
	public IFile changeContentOfFile(IProject project, IFile file, String newContent) throws Exception {
		file.setContents(new ByteArrayInputStream(newContent.getBytes(project.getDefaultCharset())), 0, null);
		return file;
	}

	/**
	 * Create a project in the local file system
	 * 
	 * @param parentFile
	 *            the parent
	 * @param projectName
	 *            project name
	 * @return the project with a location pointing to the local file system
	 * @throws Exception
	 */
	public IProject createProjectInLocalFileSystem(File parentFile, String projectName) throws Exception {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (project.exists()) {
			project.delete(true, null);
		}
		File testFile = new File(parentFile, projectName);
		if (testFile.exists())
			FileUtils.delete(testFile, FileUtils.RECURSIVE | FileUtils.RETRY);

		IProjectDescription desc = ResourcesPlugin.getWorkspace().newProjectDescription(projectName);
		desc.setLocation(new Path(new File(parentFile, projectName).getPath()));
		project.create(desc, null);
		project.open(null);
		return project;
	}

	/**
	 * verifies that repository contains exactly the given files.
	 * 
	 * @param repository
	 * @param paths
	 * @throws Exception
	 */
	public void assertRepositoryExactlyContains(Repository repository, String... paths) throws Exception {
		RepoDiff repoDiff = createRepoDiff(repository, paths);
		if (repoDiff.hasUnexpected()) {
			fail(repoDiff.getUnexpectedFiles());
		}
		if (repoDiff.hasMissing()) {
			fail(repoDiff.getUnexpectedFiles());
		}
	}

	public void assertRepositoryMisses(Repository repository, String... paths) throws Exception {
		RepoDiff repoDiff = createRepoDiff(repository, paths);
		for (String missingPath : paths) {
			assertTrue(repoDiff.getMissing().contains(missingPath));
		}
	}

	/**
	 * verifies that repository contains exactly the given files.
	 * 
	 * @param repository
	 * @param paths
	 * @throws Exception
	 */
	private RepoDiff createRepoDiff(Repository repository, String... expectedPaths) throws Exception {
		RepoDiff repoDiff = new RepoDiff();
		Set<String> expectedFiles = new HashSet<>(Arrays.asList(expectedPaths));

		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(repository.resolve("HEAD^{tree}"));
		treeWalk.setRecursive(true);

		while (treeWalk.next()) {
			String path = treeWalk.getPathString();
			if (!expectedFiles.contains(path)) {
				repoDiff.addUnexpected(path);
			}
			expectedFiles.remove(path);
		}
		repoDiff.addAllMissing(expectedFiles);
		return repoDiff;
	}

	public class RepoDiff {

		private List<String> unexpected = new ArrayList<>();
		private List<String> missing = new ArrayList<>();

		public void addMissing(String path) {
			missing.add(path);
		}

		public void addAllMissing(Collection<String> paths) {
			missing.addAll(paths);
		}

		public List<String> getMissing() {
			return missing;
		}

		public boolean hasMissing() {
			return !missing.isEmpty();
		}

		public String getMissingFiles() {
			StringBuilder builder = new StringBuilder("Repository is nissing files: ");
			for (String missingPath : getMissing()) {
				builder.append(missingPath).append(',');
			}
			return builder.toString();
		}

		public void addUnexpected(String path) {
			unexpected.add(path);
		}

		public List<String> getUnexpected() {
			return unexpected;
		}

		public boolean hasUnexpected() {
			return !unexpected.isEmpty();
		}

		public String getUnexpectedFiles() {
			StringBuilder builder = new StringBuilder("Repository contains unexpected expected files: ");
			for (String unexpectedPath : getUnexpected()) {
				builder.append(unexpectedPath).append(',');
			}
			return builder.toString();
		}
	}

	/**
	 * verifies that repository contains the given files (and eventually others)
	 * with the given content. Usage example:<br>
	 * 
	 * <code>
	 * assertRepositoryContainsFiles(repository, "foo/a.txt", "content of A",
	 *                                           "foo/b.txt", "content of B")
	 * </code>
	 * 
	 * @param repository
	 * @param fileContentToubles
	 * @throws Exception
	 * 
	 * @see #assertRepositoryContainsExactlyFilesWithContent
	 */
	public void assertRepositoryContainsFilesWithContent(Repository repository, String... fileContentToubles)
			throws Exception {
		HashMap<String, String> expectedfiles = mkmap(fileContentToubles);
		visitRepository(new Expectation(expectedfiles), repository);
	}

	private static class ExactExpectation implements GitRepoVisitor {

		private Map<String, String> expectedFiles;

		ExactExpectation(Map<String, String> expectedFiles) {
			this.expectedFiles = expectedFiles;
		}

		@Override
		public void visit(TreeWalk treeWalk) throws Exception {
			String path = treeWalk.getPathString();
			assertTrue(expectedFiles.containsKey(path));
			ObjectId objectId = treeWalk.getObjectId(0);
			byte[] expectedContent = expectedFiles.get(path).getBytes();
			byte[] repoContent = treeWalk.getObjectReader().open(objectId).getBytes();
			if (!Arrays.equals(repoContent, expectedContent)) {
				fail("File " + path + " has repository content " + new String(repoContent)
						+ " instead of expected content " + new String(expectedContent));
			}
			expectedFiles.remove(path);
		}
	}

	private static class Expectation implements GitRepoVisitor {

		private Map<String, String> expectedFiles;

		Expectation(Map<String, String> expectedFiles) {
			this.expectedFiles = expectedFiles;
		}

		@Override
		public void visit(TreeWalk treeWalk) throws Exception {
			String path = treeWalk.getPathString();
			if (!expectedFiles.containsKey(path)) {
				return;
			}

			byte[] expectedContent = expectedFiles.get(path).getBytes();
			ObjectId objectId = treeWalk.getObjectId(0);
			byte[] repoContent = treeWalk.getObjectReader().open(objectId).getBytes();
			if (!Arrays.equals(repoContent, expectedContent)) {
				fail("File " + path + " has repository content " + new String(repoContent)
						+ " instead of expected content " + new String(expectedContent));
			}
			expectedFiles.remove(path);
		}
	}

	private static interface GitRepoVisitor {
		public void visit(TreeWalk treeWalk) throws Exception;
	}

	private void visitRepository(GitRepoVisitor visitor, Repository repository) throws Exception {
		TreeWalk treeWalk = new TreeWalk(repository);
		treeWalk.addTree(repository.resolve("HEAD^{tree}"));
		treeWalk.setRecursive(true);
		while (treeWalk.next()) {
			visitor.visit(treeWalk);
		}
	}

	private static HashMap<String, String> mkmap(String... args) {
		if ((args.length % 2) > 0)
			throw new IllegalArgumentException("needs to be filepath/content pairs");
		HashMap<String, String> map = new HashMap<>();
		for (int i = 0; i < args.length; i += 2) {
			map.put(args[i], args[i + 1]);
		}
		return map;
	}

	public String getPathInRepository(IResource resource) {
		RepositoryMapping mapping = RepositoryMapping.getMapping(resource);
		if (mapping == null) {
			throw new IllegalArgumentException(resource + " is not in any repository");
		}
		return mapping.getRepoRelativePath(resource);
	}


	/**
	 * Delete a project and repeat multiple times in case of resource deletion
	 * errors. A {@link ICoreRunnable} is used to avoid concurrent activities
	 * disturbing the deletion.
	 *
	 * @param project
	 *
	 * @throws CoreException
	 */
	public static void deleteProject(IProject project) throws CoreException {
		ResourcesPlugin.getWorkspace().run(monitor -> {
			// Following code inspired by {@link
			// org.eclipse.jdt.testplugin.JavaProjectHelper#delete(IResource)}.
			// Sometimes resource deletion may fail due to concurrently held
			// locks.
			for (int i = 0; i < MAX_DELETE_RETRY; i++) {
				try {
					project.delete(
							IResource.FORCE
									| IResource.ALWAYS_DELETE_PROJECT_CONTENT,
							null);
					break;
				} catch (CoreException e) {
					if (i == MAX_DELETE_RETRY - 1) {
						throw e;
					}
					try {
						// Give other threads the time to close and release
						// the resource.
						Thread.sleep(DELETE_RETRY_DELAY);
					} catch (InterruptedException e1) {
						// Ignore and retry to delete
					}
				}
			}
		}, new NullProgressMonitor());
	}

}

