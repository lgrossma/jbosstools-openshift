/*******************************************************************************
 * Copyright (c) 2017-2020 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.test.util;

import java.security.cert.CertificateException;

import org.jboss.tools.openshift.common.core.connection.HostCertificate;
import org.jboss.tools.openshift.internal.ui.utils.SSLCertificateUtils;

public class SSLCertificateMocks {

	
	/**
	 * howto update: export the certificate via browser
	 */
	public static final String CERTIFICATE_REDHAT_COM = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIDWzCCAkOgAwIBAgIEJ97nbTANBgkqhkiG9w0BAQsFADBdMQswCQYDVQQGEwJV\n"
			+ "UzELMAkGA1UECBMCTkMxEDAOBgNVBAcTB1JhbGVpZ2gxEDAOBgNVBAoTB1JlZCBI\n"
			+ "YXQxCzAJBgNVBAsTAklUMRAwDgYDVQQDEwdSZWQgSGF0MCAXDTE4MDMxMjE3MjUy\n"
			+ "M1oYDzIwNTgwMzAyMTcyNTIzWjBdMQswCQYDVQQGEwJVUzELMAkGA1UECBMCTkMx\n"
			+ "EDAOBgNVBAcTB1JhbGVpZ2gxEDAOBgNVBAoTB1JlZCBIYXQxCzAJBgNVBAsTAklU\n"
			+ "MRAwDgYDVQQDEwdSZWQgSGF0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKC\n"
			+ "AQEAsZU8BZhreLm2fl5uEWW8fZhB/AYf9fClgIT2MJGaU8XeDKYILkOYByXvlB1T\n"
			+ "CzZpn/fJgrsyCpEvnEvv/Umk9NScrhjX6tAfAA9T4SplCGXrRhIgJ7sOiVH2AkTu\n"
			+ "0fDWcxFNQsajpKI7mnoCrGRipDI2PVy/upvwboK4OQQrAGhtOvp7GoKbJybpWS9Q\n"
			+ "giJY+besPyLLKok42hxAK7U+Y7Ylo2keipzc2VGkySPr/RfBMfQm89hnFxQd41CP\n"
			+ "H9MWbXtgFx/m4lx9kavQsIdD3/VpXchovNbDUY8AfUXKHu90aXZKJPCeDoN3YdgB\n"
			+ "KjiAt3B13zbM1u91qosXjOHx1wIDAQABoyEwHzAdBgNVHQ4EFgQUSlwS4UeTc1D9\n"
			+ "SIucRLZk747jPhcwDQYJKoZIhvcNAQELBQADggEBABq27B8wJ4E4wcwtooQKMAdQ\n"
			+ "eQiK9SwGszuNY7uovd+PQ3WiSFTIDpOFKO64Hzj1lfW/5D+hZk5M4pshkJTAHUYt\n"
			+ "nNUWKvO7Uo4jiWgbhfHv6dwiegCPikT7o4VH0EUBWh74Ec2X3KMNK5H4qAsfGqFH\n"
			+ "Yyyuvj6Im3+KtYVp9h+Ki24C99j/TLTIwV0HKL9R2YNxLLl9FdfY1fsnezKvuKxi\n"
			+ "I0QnSuu9f2VIWUDVMYDHoVfc1dvSNcAmCIpC66VnxlX9mmEHHwIfsnBrujbeigxz\n"
			+ "8C4tlJzSXn9RQ2ln7zoq1Fou3xwlTrkqVAE4/RPYLrKpsyWpEQsdgVdFd/K/3Hw=\n"
			+ "-----END CERTIFICATE-----";

	/**
	 * howto update: export the certificate via browser
	 */
	public static final String CERTIFICATE_OPEN_PAAS_REDHAT_COM = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIF1zCCBL+gAwIBAgICBVswDQYJKoZIhvcNAQELBQAwQTEQMA4GA1UECgwHUmVk\n"
			+ "IEhhdDENMAsGA1UECwwEcHJvZDEeMBwGA1UEAwwVQ2VydGlmaWNhdGUgQXV0aG9y\n"
			+ "aXR5MB4XDTE2MTAyNzExMTEwNloXDTE4MTAyNzExMTEwNlowgbcxCzAJBgNVBAYT\n"
			+ "AlVTMRcwFQYDVQQIDA5Ob3J0aCBDYXJvbGluYTEQMA4GA1UEBwwHUmFsZWlnaDEW\n"
			+ "MBQGA1UECgwNUmVkIEhhdCwgSW5jLjEfMB0GA1UECwwWSW5mb3JtYXRpb24gVGVj\n"
			+ "aG5vbG9neTEdMBsGA1UEAwwUb3Blbi5wYWFzLnJlZGhhdC5jb20xJTAjBgkqhkiG\n"
			+ "9w0BCQEWFnNlcnZpY2VkZXNrQHJlZGhhdC5jb20wggIiMA0GCSqGSIb3DQEBAQUA\n"
			+ "A4ICDwAwggIKAoICAQD4yfZ6pum3o6QFijYRh+ZQFUIiR8b2juMNf8paThy7TGir\n"
			+ "4keNSrcmxNqK+GSua9UmE7qEhxcrOsqwLf8cHDQNGvk243io+ZiwipHxEtQQ0hf7\n"
			+ "DWpEFV9ReJUIJZWMDcWRlozD32NfyGg8JwrMnFMS2onqDuYBxsMT/IcGx74Z+XF3\n"
			+ "Zgjcv4+dnOCqhV2vY75q1R4FIU0tgF4EFhzXdgBb09HuZ0KZ3F+wt4OmeBRlmBaM\n"
			+ "Yibn2cw2pCeuDCK+C1ZHDAXhzEruoEkvYIEUKLXMaWFvaYfHMmoJuNU+0S1P0gg/\n"
			+ "6A4YTAyo+r8gkvUyImHBlWhNoQGxAJWYQlj9WFR0S0QiWPLSRNTLyvx5dFj2PfRN\n"
			+ "yEchgAx5DLN7CE1F/sFg9pbeWxC/6G482/2zgrVAqu84vwgcyp8LNMDUvKn+bzMG\n"
			+ "hFNj/7TNkraq/2M1sxGnXkzW488MexEEjmjZosQhksIhjESAZqjgnbB6eKv6OEHp\n"
			+ "4WqQ+0EXAqyc19N9PnJZIBCfgM86PrPNZsnoThYFV+BSa1GlkEFEvOdlm4nZPNe1\n"
			+ "h9u0N+vQEZh1HsO0j6t84pZsod4TT+KYuLvQDbU9tXqMAnYROHIlQ9NHgJXl7cCy\n"
			+ "iGBT3tiDIT3xLi+nyulUIvLpYtTGOSSItTk3b4Q05acJai9NBC4ErO+xWIdrbQID\n"
			+ "AQABo4IBYDCCAVwwHwYDVR0jBBgwFoAUe9oJ9Uld2ddcyTb4VdIbl54RL34wOwYI\n"
			+ "KwYBBQUHAQEELzAtMCsGCCsGAQUFBzABhh9odHRwOi8vb2NzcC5yZWRoYXQuY29t\n"
			+ "L2NhL29jc3AvMA4GA1UdDwEB/wQEAwIE8DAdBgNVHSUEFjAUBggrBgEFBQcDAQYI\n"
			+ "KwYBBQUHAwIwgcwGA1UdEQSBxDCBwYIUb3Blbi5wYWFzLnJlZGhhdC5jb22CKW9w\n"
			+ "ZW5tYXN0ZXItNjctMTA3LnByb2QuYTMudmFyeS5yZWRoYXQuY29tggwxMC4yOS42\n"
			+ "Ny4xMDeCKW9wZW5tYXN0ZXItNjctMTM2LnByb2QuYTMudmFyeS5yZWRoYXQuY29t\n"
			+ "ggwxMC4yOS42Ny4xMzaCKW9wZW5tYXN0ZXItNjctMTY2LnByb2QuYTMudmFyeS5y\n"
			+ "ZWRoYXQuY29tggwxMC4yOS42Ny4xNjYwDQYJKoZIhvcNAQELBQADggEBAF/+Zz5Z\n"
			+ "XJ6nFxIbYaLA4d4ftLBOBNa0SIpjfYMkdU2+gfPTldp3rxWAnZSbDDzLqtLXLy5i\n"
			+ "CD0ZueeKM6R0EuLsrCO/YX9jhbDeyOt4SV05rtk6LsE2p7l0kRb420vFj/RIe/MN\n"
			+ "v0DjKwx25a2sHygRVg0hH92ce1TWLXGerD4G5rfWKKNbNbTbRK7s9/4L2CBPd4xN\n"
			+ "oq0T5Z7khDZpm9SghWUQdqkvv5nrquBP+xzxBL9ZDMf4IxdR1wh+Lid21MkDIUmY\n"
			+ "Gp73Qj9WA7yTi0v+gsjfBtC8rsYycuHrMpyXr9anBt2kdDD/Aj7nDLsJvLC3oRLZ\n" + "PAE6P07dzlqvUIM="
			+ "-----END CERTIFICATE-----";

	public static final String CERTIFICATE_OPENSHIFT_REDHAT_COM = "-----BEGIN CERTIFICATE-----\n"
			+ "MIIG5jCCBc6gAwIBAgIQRO2nIIKBcNnRxDIPrJlSZDANBgkqhkiG9w0BAQsFADBE"
			+ "MQswCQYDVQQGEwJVUzEWMBQGA1UEChMNR2VvVHJ1c3QgSW5jLjEdMBsGA1UEAxMU"
			+ "R2VvVHJ1c3QgU1NMIENBIC0gRzMwHhcNMTYxMDI1MDAwMDAwWhcNMTcxMjI0MjM1"
			+ "OTU5WjBpMQswCQYDVQQGEwJVUzEXMBUGA1UECAwOTm9ydGggQ2Fyb2xpbmExEDAO"
			+ "BgNVBAcMB1JhbGVpZ2gxEDAOBgNVBAoMB1JlZCBIYXQxHTAbBgNVBAMMFG9wZW5z"
			+ "aGlmdC5yZWRoYXQuY29tMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA"
			+ "zfv/3KqG8hKPK7sefMara99z/vSN22T6qWnk2+BZ8WtV95w6o7PhcD2DcR2pG9v4"
			+ "3hXKryaE8zTRi+18mYK+UQ+zxmtuU7XeZJEboljsFboAEg+dhU7RE3S24TLg4fWa"
			+ "0BIuUUwXo6Dvr6UYuYMtehKJqwXEZosDBZg2lTAY4KJEza2rpoN+hcCd2QiJlcWl"
			+ "36uDI2hdpnfifXa0DFUaipV4USaN//D3Cm3Br/c+seFwkGT5s8NC8H8gX1ilEv+m"
			+ "h0tMDl2upRWt2qaJeKLWcuWixWXgg9zkF0kXGq2DOkk+4nh6xceUn7Mio89UREdo"
			+ "da2oevLqFdaA3AS2VeHHwwIDAQABo4IDrTCCA6kwggEbBgNVHREEggESMIIBDoIV"
			+ "ZGV2ZWxvcGVycy5yZWRoYXQuY29tghBzdGF0aWMuamJvc3Mub3JnghNkb3dubG9h"
			+ "ZHMuamJvc3Mub3JnghhpdC1kZXZlbG9wZXJzLnJlZGhhdC5jb22CGGVudGVycHJp"
			+ "c2Uub3BlbnNoaWZ0LmNvbYINb3BlbnNoaWZ0LmNvbYITcmVwby5mdXNlc291cmNl"
			+ "LmNvbYISZG93bmxvYWQuamJvc3Mub3Jngg13d3cuamJvc3Mub3JnghF3d3cub3Bl"
			+ "bnNoaWZ0LmNvbYIUYXNzZXRzLm9wZW5zaGlmdC5uZXSCFGRldmVsb3Blci5yZWRo"
			+ "YXQuY29tghRvcGVuc2hpZnQucmVkaGF0LmNvbTAJBgNVHRMEAjAAMA4GA1UdDwEB"
			+ "/wQEAwIFoDArBgNVHR8EJDAiMCCgHqAchhpodHRwOi8vZ24uc3ltY2IuY29tL2du"
			+ "LmNybDCBnQYDVR0gBIGVMIGSMIGPBgZngQwBAgIwgYQwPwYIKwYBBQUHAgEWM2h0"
			+ "dHBzOi8vd3d3Lmdlb3RydXN0LmNvbS9yZXNvdXJjZXMvcmVwb3NpdG9yeS9sZWdh"
			+ "bDBBBggrBgEFBQcCAjA1DDNodHRwczovL3d3dy5nZW90cnVzdC5jb20vcmVzb3Vy"
			+ "Y2VzL3JlcG9zaXRvcnkvbGVnYWwwHQYDVR0lBBYwFAYIKwYBBQUHAwEGCCsGAQUF"
			+ "BwMCMB8GA1UdIwQYMBaAFNJv95b0hT9yPDB9I9qFeJujfFp8MFcGCCsGAQUFBwEB"
			+ "BEswSTAfBggrBgEFBQcwAYYTaHR0cDovL2duLnN5bWNkLmNvbTAmBggrBgEFBQcw"
			+ "AoYaaHR0cDovL2duLnN5bWNiLmNvbS9nbi5jcnQwggEFBgorBgEEAdZ5AgQCBIH2"
			+ "BIHzAPEAdgDd6x0reg1PpiCLga2BaHB+Lo6dAdVciI09EcTNtuy+zAAAAVf9bZ3I"
			+ "AAAEAwBHMEUCIQDS3QAdPSnqM2wnjw7A1p3sg/QZaH6zXAxD+lbpbP64AwIgI128"
			+ "s7Td+zsM1UQHsVO/4GiITt3fXwuSgI54hF0+kWwAdwBo9pj4H2SCvjqM7rkoHUz8"
			+ "cVFdZ5PURNEKZ6y7T0/7xAAAAVf9bZ3lAAAEAwBIMEYCIQC56UEZDasbO5hoy/9k"
			+ "yDcI9/9TGzvXviYgx9n94wEiawIhAJWeXIAGgp1Mxcj0E9Fk1Tg0wOjjKn5WkWTq"
			+ "t3TSXckhMA0GCSqGSIb3DQEBCwUAA4IBAQA0P2C0uZvQDvg/CWX2/gqHhjicLUcJ"
			+ "ASI7bh7fcrsHRI4X7qTY2oHDs0NFGPXygL+XQ3KxJAWuSRBhaC7epCFhEsDXxC1k"
			+ "lOEe5L9JFUUOuc1/ja2rLQgRU5nZ/+aYhGAPYpxIWTmYGy9xLicbpU+bsvVt2P8y"
			+ "iy3z1rF431L76cUe/W7SLPF2DG9QxRcv+krD3GpRsr+WLGXPY3YK6PrzmlAW/c42"
			+ "9z1VUHA0WN2md2OMvIe43q6X34+c0TzkAGEKB/xRB2+l+mwzWqGCM1vHy0ca0gBU"
			+ "0sPgQdW1KicwD7C89KlqqaNm0FsnOo9n8GldXd4+JpliydsipmkEQUxU" + "-----END CERTIFICATE-----";

	public static HostCertificate createHostCertificate(boolean accepted, String certificateContent)
			throws CertificateException {
		return new HostCertificate(accepted, SSLCertificateUtils.createX509Certificate(certificateContent));
	}
}
