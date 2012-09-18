/*******************************************************************************
 * Copyright (c) 2012 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard.ssh;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.validation.MultiValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.jboss.tools.openshift.express.internal.ui.utils.StringUtils;

import com.openshift.client.OpenShiftException;
import com.openshift.client.SSHPublicKey;

/**
 * @author Andre Dietisheim
 */
public class SSHPublicKeyValidator extends MultiValidator {

	private IObservableValue filePathObservable;
	private AddSSHKeyWizardPageModel model;

	public SSHPublicKeyValidator(IObservableValue filePathObservable, AddSSHKeyWizardPageModel model) {
		this.filePathObservable = filePathObservable;
		this.model = model;
	}

	@Override
	protected IStatus validate() {
		String filePath = (String) filePathObservable.getValue();
		if (StringUtils.isEmpty(filePath)) {
			return ValidationStatus.cancel("You have to supply a public SSH key.");
		}
		try {
			SSHPublicKey sshPublicKey = new SSHPublicKey(filePath);
			if (model.hasPublicKey(sshPublicKey.getPublicKey())) {
				return ValidationStatus.error("The public key in " + filePath + " is already in use on OpenShift. Choose another key.");
			}
		} catch (FileNotFoundException e) {
			return ValidationStatus.error("Could not load file: " + e.getMessage());
		} catch (OpenShiftException e) {
			return ValidationStatus.error(filePath + "is not a valid public SSH key: " + e.getMessage());
		} catch (IOException e) {
			return ValidationStatus.error("Could not load file: " + e.getMessage());
		}

		return Status.OK_STATUS;
	}

}
