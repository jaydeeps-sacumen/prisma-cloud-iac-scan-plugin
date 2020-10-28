package org.jenkinsci.plugins.shell.prismacloud;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;


import com.prismacloud.config.PrismaCloudConfiguration;
import com.prismacloud.service.impl.PrismaCloudServiceImpl;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TemplateScanBuilderTest {

  @Mock
  PrintStream printStream;
  @Mock
  PrismaCloudServiceImpl prismaCloudServiceImpl;
  @Rule
  public TemporaryFolder temp = new TemporaryFolder();

  TemplateScanBuilder templateScanBuilder;
  TaskListener listener;
  FilePath workspace;
  Launcher launcher;
  String scanResult;
  Run build;
  Map<String, String> propertiesMap;
  File tmpFile;

  @Before
  public void setUp() throws IOException {
    build = Mockito.mock(Run.class, Mockito.CALLS_REAL_METHODS);
    launcher = Mockito.mock(Launcher.class, Mockito.CALLS_REAL_METHODS);
    listener = Mockito.mock(TaskListener.class, Mockito.CALLS_REAL_METHODS);
    propertiesMap = new HashMap<>();
    propertiesMap.put("prisma_cloud_cicd_failure_criteria", "High: 3, Medium: 17, Low: 1000, Operator: and");
    tmpFile = temp.newFile();
    workspace = new FilePath(tmpFile.getAbsoluteFile());
    scanResult = "{\"meta\":{\"matchedPoliciesSummary\":{\"high\":4,\"medium\":18,\"low\":0}},\"data\":[{\"id\":\"1da48a52-fc22-414f-a1bb-f864d7fdfc77\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure Key Vault secrets have no expiration date\",\"rule\":\"$.resource.azurerm_key_vault_secret[*].expiration_date anyNull\",\"desc\":\"This policy identifies Azure Key Vault secrets that do not have an expiry date. As a best practice, set an expiration date for each secret and rotate the secret regularly.\\n\\nBefore you activate this policy, ensure that you have added the Prisma Cloud Servic\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"1da48a52-fc22-414f-a1bb-f864d7fdfc77\"}},{\"id\":\"b1eec428-ad10-4206-a40e-916dbb0a76bd\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app client certificate is disabled\",\"rule\":\"$.resource.azurerm_app_service exists and ($.resource.azurerm_app_service[*].client_cert_enabled anyNull or $.resource.azurerm_app_service[*].client_cert_enabled anyFalse)\",\"desc\":\"This policy identifies Azure web apps which are not set with client certificate. Client certificates allow for the app to request a certificate for incoming requests. Only clients that have a valid certificate will be able to reach the app.\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"b1eec428-ad10-4206-a40e-916dbb0a76bd\"}},{\"id\":\"fde9482f-3ac2-43f6-bda2-bf2013074acd\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure storage account logging for queues is disabled\",\"rule\":\"$.resource.azurerm_storage_account[*].queue_properties.logging.* size > 0 and ($.resource.azurerm_storage_account[*].queue_properties.logging.delete anyFalse or $.resource.azurerm_storage_account[*].queue_properties.logging.read anyFalse or $.resource.azurerm_storage_account[*].queue_properties.logging.write anyFalse )\",\"desc\":\"Storage Logging records details of requests (read, write, and delete operations) against your Azure queues. The logs include additional information such as:\\n- Timing and server latency.\\n- Success or failure, and HTTP status code.\\n- Authentication details\\n\\n\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"fde9482f-3ac2-43f6-bda2-bf2013074acd\"}},{\"id\":\"7cc2b77b-ad71-4a84-8cab-66b2b04eea5f\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app doesn't redirect HTTP to HTTPS\",\"rule\":\"$.resource.azurerm_app_service.* size > 0 and ($.resource.azurerm_app_service[*].https_only anyNull or $.resource.azurerm_app_service[*].https_only anyFalse)\",\"desc\":\"Azure Web Apps allows sites to run under both HTTP and HTTPS by default. Web apps can be accessed by anyone using non-secure HTTP links by default. Non-secure HTTP requests can be restricted and all HTTP requests redirected to the secure HTTPS port. It is \",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"7cc2b77b-ad71-4a84-8cab-66b2b04eea5f\"}},{\"id\":\"09fcb4f7-59f3-4101-a717-d4f5a5235067\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure Network Watcher Network Security Group (NSG) flow logs retention is less than 90 days\",\"rule\":\"$.resource.azurerm_network_security_group.* size greater than 0 and ($.resource.azurerm_network_watcher_flow_log.* size equals 0 or $.resource.azurerm_network_watcher_flow_log[*].enabled anyNull or $.resource.azurerm_network_watcher_flow_log[*].enabled anyFalse or $.resource.azurerm_network_watcher_flow_log[*].retention_policy.enabled anyFalse or $.resource.azurerm_network_watcher_flow_log[*].retention_policy[?( @.days<90 )] size greater than 0)\",\"desc\":\"This policy identifies Azure Network Security Groups (NSG) for which flow logs retention period is 90 days or less. To perform this check, enable this action on the Azure Service Principal: 'Microsoft.Network/networkWatchers/queryFlowLogStatus/action'.\\n\\nNS\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"09fcb4f7-59f3-4101-a717-d4f5a5235067\"}},{\"id\":\"3d8d4e24-1336-4bc1-a1f3-15e680edca07\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure Storage Account 'Trusted Microsoft Services' access not enabled\",\"rule\":\"$.resource.azurerm_storage_account.* size greater than 0 and ($.resource.azurerm_storage_account[*].network_rules anyNull or $.resource.azurerm_storage_account[*].network_rules.bypass anyNull or not ( $.resource.azurerm_storage_account[*].network_rules.bypass allEqual \\\"AzureServices\\\" ))\",\"desc\":\"Some Microsoft services that interact with storage accounts operate from networks that can't be granted access through network rules. To help this type of service work as intended, allow the set of trusted Microsoft services to bypass the network rules. Th\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"3d8d4e24-1336-4bc1-a1f3-15e680edca07\"}},{\"id\":\"5e94790e-0d8b-4001-b97f-b5f7670a9236\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app authentication is off\",\"rule\":\"$.resource.azurerm_app_service exists and ($.resource.azurerm_app_service[*].auth_settings.enabled anyFalse or $.resource.azurerm_app_service[*].auth_settings anyNull)\",\"desc\":\"Azure App Service Authentication is a feature that can prevent anonymous HTTP requests from reaching the API app, or authenticate those that have tokens before they reach the API app. If an anonymous request is received from a browser, App Service will red\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"5e94790e-0d8b-4001-b97f-b5f7670a9236\"}},{\"id\":\"86eb5c4f-d384-4cb0-b5d8-7dc007bb4804\",\"attributes\":{\"severity\":\"high\",\"name\":\"Azure SQL Server auditing is disabled\",\"rule\":\"$.resource.azurerm_sql_database.* size greater than 0 and ($.resource.azurerm_sql_database[*].threat_detection_policy anyNull or $.resource.azurerm_sql_database[*].threat_detection_policy.state anyEqual Disabled)\",\"desc\":\"Audit logs can help you find suspicious events, unusual activity, and trends to analyze database events. Auditing the SQL Server, at the server-level, enables you to track all new and existing databases on the server.\\n\\nThis policy identifies SQL servers do\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"86eb5c4f-d384-4cb0-b5d8-7dc007bb4804\"}},{\"id\":\"4f5c4a28-c3df-4bee-a980-621c794548ed\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app doesn't use HTTP 2.0\",\"rule\":\"$.resource.azurerm_app_service.* size > 0 and ($.resource.azurerm_app_service[*].*.http2_enabled anyNull or $.resource.azurerm_app_service[*].*.http2_enabled anyFalse)\",\"desc\":\"HTTP 2.0 has additional performance improvements on the head-of-line blocking problem of old HTTP version, header compression, and prioritization of requests. HTTP 2.0 no longer supports HTTP 1.1's chunked transfer encoding mechanism, as it provides its ow\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"4f5c4a28-c3df-4bee-a980-621c794548ed\"}},{\"id\":\"4169132e-ead6-4c01-b147-d2b47b443678\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure SQL Server advanced data security is disabled\",\"rule\":\" $.resource.azurerm_sql_server.* size greater than 0 and ($.resource.azurerm_mssql_server_security_alert_policy.* size == 0 or  $.resource.azurerm_mssql_server_security_alert_policy[*].state anyEqual \\\"Disabled\\\" or $.resource.azurerm_mssql_server_security_alert_policy[*].retention_days anyNull )\",\"desc\":\"Advanced data security (ADS) provides a set of advanced SQL security capabilities, including vulnerability assessment, threat detection, and data discovery and classification.\\n\\nThis policy identifies Azure SQL servers that do not have ADS enabled. As a bes\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"4169132e-ead6-4c01-b147-d2b47b443678\"}},{\"id\":\"7a506ab4-d0a2-48ee-a6f5-75a97f11397d\",\"attributes\":{\"severity\":\"high\",\"name\":\"Azure storage account has a blob container with public access\",\"rule\":\"$.resource.azurerm_storage_blob.* size greater than 0 and $.resource.azurerm_storage_container.* size greater than 0 and $.resource.azurerm_storage_container[*].container_access_type anyEqual blob or $.resource.azurerm_storage_container[*].container_access_type anyEqual container\",\"desc\":\"This policy identifies blob containers within an Azure storage account that allow anonymous/public access ('CONTAINER' or 'BLOB'). As a best practice, do not allow anonymous/public access to blob containers unless you have a very good reason. Instead, you \",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"7a506ab4-d0a2-48ee-a6f5-75a97f11397d\"}},{\"id\":\"a9937384-1ee3-430c-acda-fb97e357bfcd\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Activity Log Retention should not be set to less than 365 days\",\"rule\":\"$.resource.azurerm_monitor_log_profile.* size greater than 0 and ( $.resource.azurerm_monitor_log_profile[*].retention_policy.* size equals 0 or $.resource.azurerm_monitor_log_profile[*].retention_policy.enabled anyFalse or $.resource.azurerm_monitor_log_profile[*].retention_policy[?(@.days<365)] size greater than 0 )\",\"desc\":\"A Log Profile controls how your Activity Log is exported and retained. Since the average time to detect a breach is over 200 days, it is recommended to retain your activity log for 365 days or more in order to have time to respond to any incidents.\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"a9937384-1ee3-430c-acda-fb97e357bfcd\"}},{\"id\":\"d265abb4-03b6-44fd-826f-1b769617077f\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure SQL Server threat detection alerts not enabled for all threat types\",\"rule\":\"$.resource.azurerm_sql_database.* size greater than 0 and $.resource.azurerm_sql_database[*].threat_detection_policy.* size greater than 0 and $.resource.azurerm_sql_database[*].threat_detection_policy.disabled_alerts.* size greater than 0\",\"desc\":\"Advanced data security (ADS) provides a set of advanced SQL security capabilities, including vulnerability assessment, threat detection, and data discovery and classification.\\n\\nThis policy identifies Azure SQL servers that have disabled the detection of on\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"d265abb4-03b6-44fd-826f-1b769617077f\"}},{\"id\":\"0ca00469-8223-4753-a9df-4add7c37725f\",\"attributes\":{\"severity\":\"high\",\"name\":\"Azure SQL Server audit log retention is less than 91 days\",\"rule\":\"$.resource.azurerm_sql_database.* size greater than 0 and $.resource.azurerm_sql_database[*].threat_detection_policy.* size greater than 0 and ($.resource.azurerm_sql_database[*].threat_detection_policy.retention_days anyNull or $.resource.azurerm_sql_database[*].threat_detection_policy[?( @.retention_days<91 )] size greater than 0)\",\"desc\":\"Audit Logs can help you find suspicious events, unusual activity, and trends. Auditing the SQL server, at the server-level, allows you to track all existing and newly created databases on the instance.\\n\\nThis policy identifies SQL servers which do not retai\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"0ca00469-8223-4753-a9df-4add7c37725f\"}},{\"id\":\"c9095cf0-3233-4cf8-af1e-ce9457a3a22a\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure SQL Server advanced data security does not send alerts to service and co-administrators\",\"rule\":\"$.resource.azurerm_sql_database exists and ($.resource.azurerm_sql_database[*].threat_detection_policy anyNull or $.resource.azurerm_sql_database[*].threat_detection_policy.state anyEqual Disabled or $.resource.azurerm_sql_database[*].threat_detection_policy.email_account_admins  anyNull or $.resource.azurerm_sql_database[*].threat_detection_policy.email_account_admins anyFalse)\",\"desc\":\"Advanced data security (ADS) provides a set of advanced SQL security capabilities, including vulnerability assessment, threat detection, and data discovery and classification.\\n\\nThis policy identifies Azure SQL Servers that are not enabled with ADS. As a be\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"c9095cf0-3233-4cf8-af1e-ce9457a3a22a\"}},{\"id\":\"3b6626af-9601-4e99-ace5-7197cba0d37d\",\"attributes\":{\"severity\":\"high\",\"name\":\"Azure AKS enable role-based access control (RBAC) not enforced\",\"rule\":\"$.resource.azurerm_kubernetes_cluster exists and ($.resource.azurerm_kubernetes_cluster[*].role_based_access_control anyNull or $.resource.azurerm_kubernetes_cluster[*].role_based_access_control.enabled anyFalse)\",\"desc\":\"To provide granular filtering of the actions that users can perform, Kubernetes uses role-based access controls (RBAC). This control mechanism lets you assign users, or groups of users, permission to do things like create or modify resources, or view logs \",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"3b6626af-9601-4e99-ace5-7197cba0d37d\"}},{\"id\":\"629133a3-6e81-4288-bd38-e81cb5b708de\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app doesn't use latest .Net Core version\",\"rule\":\"$.resource.azurerm_app_service[*].site_config[?( @.dotnet_framework_version !='v4.0' && @.dotnet_framework_version )] size greater than 0\",\"desc\":\"Periodically, newer versions are released for .Net Core software either due to security flaws or to include additional functionality. Using the latest .Net Core version for web apps is recommended in order to take advantage of security fixes, if any.\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"629133a3-6e81-4288-bd38-e81cb5b708de\"}},{\"id\":\"74e43b65-16bf-42a5-8d10-a0f245716cde\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app doesn't use latest TLS version\",\"rule\":\"$.resource.azurerm_app_service[*].site_config[?(  @.min_tls_version!='1.2' && @.min_tls_version )] size greater than 0\",\"desc\":\"This policy identifies Azure web apps which are not set with latest version of TLS encryption. App service currently allows the web app to set TLS versions 1.0, 1.1 and 1.2. It is highly recommended to use the latest TLS 1.2 version for web app secure conn\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"74e43b65-16bf-42a5-8d10-a0f245716cde\"}},{\"id\":\"bc4e467f-10fa-471e-aa9b-28981dc73e93\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Storage Accounts without Secure transfer enabled\",\"rule\":\"$.resource.azurerm_storage_account exists and ($.resource.azurerm_storage_account[*].enable_https_traffic_only anyNull or $.resource.azurerm_storage_account[*].enable_https_traffic_only anyFalse)\",\"desc\":\"The secure transfer option enhances the security of your storage account by only allowing requests to the storage account by a secure connection. For example, when calling REST APIs to access your storage accounts, you must connect using HTTPs. Any request\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"bc4e467f-10fa-471e-aa9b-28981dc73e93\"}},{\"id\":\"329e3b79-b374-4434-b7c8-4d292aa4f991\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure App Service Web app doesn't have a Managed Service Identity\",\"rule\":\"$.resource.azurerm_app_service[*].identity anyNull\",\"desc\":\"Managed service identity in App Service makes the app more secure by eliminating secrets from the app, such as credentials in the connection strings. When registering with Azure Active Directory in the app service, the app will connect to other Azure servi\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"329e3b79-b374-4434-b7c8-4d292aa4f991\"}},{\"id\":\"53f26264-40e0-462b-8305-bd1455bf925f\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure Virtual Machine does not have endpoint protection installed\",\"rule\":\"$.resource.azurerm_virtual_machine.* size greater than 0 and $.resource.azurerm_virtual_machine_extension[*].type does not contain EndpointSecurity and $.resource.azurerm_virtual_machine_extension[*].type does not contain TrendMicroDSA and $.resource.azurerm_virtual_machine_extension[*].type does not contain Antimalware and $.resource.azurerm_virtual_machine_extension[*].type does not contain EndpointProtection and $.resource.azurerm_virtual_machine_extension[*].type does not contain SCWPAgent and $.resource.azurerm_virtual_machine_extension[*].type does not contain PortalProtectExtension and $.resource.azurerm_virtual_machine_extension[*].type does not contain FileSecurity\",\"desc\":\"This policy identifies Azure Virtual Machines (VMs) that do not have endpoint protection installed. Installing endpoint protection systems (like Antimalware for Azure) provides for real-time protection capability that helps identify and remove viruses, spy\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"53f26264-40e0-462b-8305-bd1455bf925f\"}},{\"id\":\"991aca47-286f-45be-8737-ff9c069beab6\",\"attributes\":{\"severity\":\"medium\",\"name\":\"Azure Storage Account default network access is set to 'Allow'\",\"rule\":\"$.resource.azurerm_storage_account.* size greater than 0 and (  $.resource.azurerm_storage_account_network_rules[*].default_action anyEqual \\\"Allow\\\" or $.resource.azurerm_storage_account[*].network_rules.default_action  anyEqual \\\"Allow\\\" )\",\"desc\":\"Restricting default network access helps to provide a new layer of security, since storage accounts accept connections from clients on any network. To limit access to selected networks, the default action must be changed.\",\"files\":[\"./azure_all_issues.tf\"],\"policyId\":\"991aca47-286f-45be-8737-ff9c069beab6\"}}]}";
  }


  @Test
  public void testPerform() throws IOException, InterruptedException {
    templateScanBuilder = spy(new TemplateScanBuilder("localhost","5","0","19", "and","MAVEN_test", "me.sacumen; team: plugin", "tf", "1.0"));
    prismaCloudServiceImpl = spy(new PrismaCloudServiceImpl());

    Mockito.doReturn(scanResult).when(prismaCloudServiceImpl).getScanDetails(any(PrismaCloudConfiguration.class), any(String.class));
    Mockito.doReturn(printStream).when(listener).getLogger();
    Mockito.doReturn(scanResult).when(templateScanBuilder).callPrismaCloudAsyncEndPoint(any(String.class),any(TaskListener.class), any(String.class));
    Mockito.doReturn(true).when(templateScanBuilder).checkSeverity(any(String.class), any(TaskListener.class));
    Mockito.doReturn(propertiesMap).when(templateScanBuilder).getSevirityMap(any(String.class),any(String.class),any(String.class),any(String.class));
    Mockito.doReturn(true).when(templateScanBuilder).createZipFile(any(File.class), any(File.class));
    templateScanBuilder.perform(build, workspace, launcher, listener);
  }
}