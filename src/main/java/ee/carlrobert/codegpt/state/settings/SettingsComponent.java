package ee.carlrobert.codegpt.state.settings;

import com.intellij.openapi.ui.ComboBox;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UI;
import ee.carlrobert.codegpt.util.SwingUtils;
import ee.carlrobert.openai.client.completion.CompletionModel;
import ee.carlrobert.openai.client.completion.chat.ChatCompletionModel;
import ee.carlrobert.openai.client.completion.text.TextCompletionModel;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.event.HyperlinkEvent;

public class SettingsComponent {

  private final JPanel mainPanel;
  private final JBTextField apiKeyField;
  private final JBTextField organizationField;
  private final JBTextField resourceNameField;
  private final JBTextField deploymentIdField;
  private final JBTextField apiVersionField;
  private final JBTextField displayNameField;
  private final JBCheckBox useOpenAIAccountNameCheckBox;
  private final JBCheckBox useActiveDirectoryAuthenticationCheckBox;
  private final JBRadioButton useAzureServiceRadioButton;
  private final JBRadioButton useOpenAIServiceRadioButton;
  private final ComboBox<CompletionModel> chatCompletionBaseModelComboBox;
  private final ComboBox<CompletionModel> textCompletionBaseModelComboBox;
  private final JBRadioButton useChatCompletionRadioButton;
  private final JBRadioButton useTextCompletionRadioButton;

  public SettingsComponent(SettingsState settings) {
    apiKeyField = new JBTextField(settings.apiKey, 40);
    useOpenAIServiceRadioButton = new JBRadioButton("Use OpenAI service API",
        settings.useOpenAIService);
    useAzureServiceRadioButton = new JBRadioButton("Use Azure OpenAI service API",
        settings.useAzureService);
    resourceNameField = new JBTextField(settings.resourceName, 40);
    deploymentIdField = new JBTextField(settings.resourceName, 40);
    apiVersionField = new JBTextField(settings.resourceName, 40);
    organizationField = new JBTextField(settings.organization, 40);
    displayNameField = new JBTextField(settings.displayName, 20);
    useOpenAIAccountNameCheckBox = new JBCheckBox(
        "Use OpenAI account name", settings.useOpenAIAccountName);
    useActiveDirectoryAuthenticationCheckBox = new JBCheckBox(
        "Use Azure Active Directory authentication", settings.useActiveDirectoryAuthentication);
    chatCompletionBaseModelComboBox = new BaseModelComboBox(
        new ChatCompletionModel[]{
            ChatCompletionModel.GPT_3_5,
            ChatCompletionModel.GPT_3_5_SNAPSHOT,
            ChatCompletionModel.GPT_4,
            ChatCompletionModel.GPT_4_32k
        },
        ChatCompletionModel.findByCode(settings.chatCompletionBaseModel));
    textCompletionBaseModelComboBox = new BaseModelComboBox(
        new TextCompletionModel[]{
            TextCompletionModel.DAVINCI,
            TextCompletionModel.CURIE,
            TextCompletionModel.BABBAGE,
            TextCompletionModel.ADA,
        },
        TextCompletionModel.findByCode(settings.textCompletionBaseModel));
    useChatCompletionRadioButton = new JBRadioButton("Use chat completion",
        settings.isChatCompletionOptionSelected);
    useTextCompletionRadioButton = new JBRadioButton("Use text completion",
        settings.isTextCompletionOptionSelected);
    mainPanel = FormBuilder.createFormBuilder()
        .addComponent(new TitledSeparator("Integration Preference"))
        .addVerticalGap(8)
        .addComponent(createMainSelectionForm())
        .addVerticalGap(8)
        .addComponent(new TitledSeparator("Service Preference"))
        .addVerticalGap(8)
        .addComponent(createServiceSelectionForm())
        .addVerticalGap(8)
        .addComponent(new TitledSeparator("Model Preference"))
        .addVerticalGap(8)
        .addComponent(createModelSelectionForm())
        .addComponentFillVertically(new JPanel(), 0)
        .getPanel();

    registerCompletionButtons();
    registerFields();
  }

  public JPanel getPanel() {
    return mainPanel;
  }

  public JComponent getPreferredFocusedComponent() {
    return apiKeyField;
  }

  public String getApiKey() {
    return apiKeyField.getText();
  }

  public void setApiKey(String apiKey) {
    apiKeyField.setText(apiKey);
  }

  public void setUseOpenAIServiceSelected(boolean selected) {
    useOpenAIServiceRadioButton.setSelected(selected);
  }

  public boolean isUseOpenAIService() {
    return useOpenAIServiceRadioButton.isSelected();
  }

  public void setUseAzureServiceSelected(boolean selected) {
    useAzureServiceRadioButton.setSelected(selected);
  }

  public boolean isUseActiveDirectoryAuthentication() {
    return useActiveDirectoryAuthenticationCheckBox.isSelected();
  }

  public void setUseActiveDirectoryAuthenticationSelected(boolean selected) {
    useActiveDirectoryAuthenticationCheckBox.setSelected(selected);
  }

  public boolean isUseAzureService() {
    return useAzureServiceRadioButton.isSelected();
  }

  public String getResourceName() {
    return resourceNameField.getText();
  }

  public void setResourceName(String resourceName) {
    resourceNameField.setText(resourceName);
  }

  public String getDeploymentId() {
    return deploymentIdField.getText();
  }

  public void setDeploymentId(String deploymentId) {
    deploymentIdField.setText(deploymentId);
  }

  public String getApiVersion() {
    return apiVersionField.getText();
  }

  public void setApiVersionField(String apiVersion) {
    apiVersionField.setText(apiVersion);
  }

  public String getOrganization() {
    return organizationField.getText();
  }

  public void setOrganization(String organization) {
    organizationField.setText(organization);
  }

  public String getDisplayName() {
    return displayNameField.getText();
  }

  public void setDisplayName(String displayName) {
    displayNameField.setText(displayName);
  }

  public void setUseOpenAIAccountNameCheckBox(boolean selected) {
    useOpenAIAccountNameCheckBox.setSelected(selected);
  }

  public boolean isUseOpenAIAccountName() {
    return useOpenAIAccountNameCheckBox.isSelected();
  }

  public boolean isChatCompletionOptionSelected() {
    return useChatCompletionRadioButton.isSelected();
  }

  public void setUseChatCompletionSelected(boolean isSelected) {
    useChatCompletionRadioButton.setSelected(isSelected);
  }

  public boolean isTextCompletionOptionSelected() {
    return useTextCompletionRadioButton.isSelected();
  }

  public void setUseTextCompletionSelected(boolean isSelected) {
    useTextCompletionRadioButton.setSelected(isSelected);
  }

  public TextCompletionModel getTextCompletionBaseModel() {
    return (TextCompletionModel) textCompletionBaseModelComboBox.getSelectedItem();
  }

  public void setTextCompletionBaseModel(String modelCode) {
    textCompletionBaseModelComboBox.setSelectedItem(TextCompletionModel.findByCode(modelCode));
  }

  public ChatCompletionModel getChatCompletionBaseModel() {
    return (ChatCompletionModel) chatCompletionBaseModelComboBox.getSelectedItem();
  }

  public void setChatCompletionBaseModel(String modelCode) {
    chatCompletionBaseModelComboBox.setSelectedItem(ChatCompletionModel.findByCode(modelCode));
  }

  private JPanel createMainSelectionForm() {
    var apiKeyFieldPanel = UI.PanelFactory.panel(apiKeyField)
        .withLabel("API key:")
        .resizeX(false)
        .withComment(
            "You can find your Secret API key in your <a href=\"https://platform.openai.com/account/api-keys\">User settings</a>.")
        .withCommentHyperlinkListener(this::handleHyperlinkClicked)
        .createPanel();
    var displayNameFieldPanel = SwingUtils.createPanel(displayNameField, "Display name:", false);

    SwingUtils.setEqualLabelWidths(apiKeyFieldPanel, displayNameFieldPanel);

    var panel = FormBuilder.createFormBuilder()
        .addComponent(FormBuilder.createFormBuilder()
            .addComponent(apiKeyFieldPanel)
            .addComponent(displayNameFieldPanel)
            .addComponent(useOpenAIAccountNameCheckBox)
            .getPanel())
        .getPanel();
    panel.setBorder(JBUI.Borders.emptyLeft(16));
    return panel;
  }

  private JPanel createServiceSelectionForm() {
    var organizationFieldPanel = UI.PanelFactory.panel(organizationField)
        .withLabel("Organization:")
        .resizeX(false)
        .withComment(
            "<span>Useful when you are part of multiple organizations <sup><strong>optional</strong></sup></span")
        .createPanel();
    organizationFieldPanel.setBorder(JBUI.Borders.empty(8, 16, 0, 0));
    var azureRelatedFieldsPanel = createAzureServicePanel();

    registerServiceButtons(azureRelatedFieldsPanel, organizationFieldPanel);

    var panel = FormBuilder.createFormBuilder()
        .addComponent(FormBuilder.createFormBuilder()
            .addComponent(useOpenAIServiceRadioButton)
            .addComponent(organizationFieldPanel)
            .addVerticalGap(8)
            .addComponent(useAzureServiceRadioButton)
            .addComponent(azureRelatedFieldsPanel)
            .getPanel())
        .getPanel();
    panel.setBorder(JBUI.Borders.emptyLeft(16));
    return panel;
  }

  private JPanel createModelSelectionForm() {
    var chatCompletionModelsPanel = SwingUtils.createPanel(
        chatCompletionBaseModelComboBox, "Model:", false);
    chatCompletionModelsPanel.setBorder(JBUI.Borders.emptyLeft(16));

    var textCompletionModelsPanel = SwingUtils.createPanel(
        textCompletionBaseModelComboBox, "Model:", false);
    textCompletionModelsPanel.setBorder(JBUI.Borders.emptyLeft(16));

    var panel = FormBuilder.createFormBuilder()
        .addComponent(FormBuilder.createFormBuilder()
            .addComponent(UI.PanelFactory.panel(useChatCompletionRadioButton)
                .withComment("OpenAI’s most advanced language model")
                .createPanel())
            .addComponent(chatCompletionModelsPanel)
            .addComponent(UI.PanelFactory.panel(useTextCompletionRadioButton)
                .withComment("Best for high-quality texts")
                .createPanel())
            .addComponent(textCompletionModelsPanel)
            .getPanel())
        .getPanel();
    panel.setBorder(JBUI.Borders.emptyLeft(16));
    return panel;
  }

  private JPanel createAzureServicePanel() {
    JPanel azureRelatedFieldsPanel = new JPanel();
    var resourceNameFieldPanel = UI.PanelFactory.panel(resourceNameField)
        .withLabel("Resource name:")
        .resizeX(false)
        .withComment(
            "Azure OpenAI Service resource name")
        .createPanel();
    var deploymentIdFieldPanel = UI.PanelFactory.panel(deploymentIdField)
        .withLabel("Deployment ID:")
        .resizeX(false)
        .withComment(
            "Azure OpenAI Service deployment ID")
        .createPanel();
    var apiVersionFieldPanel = UI.PanelFactory.panel(apiVersionField)
        .withLabel("API version:")
        .resizeX(false)
        .withComment(
            "API version to be used for Azure OpenAI Service")
        .createPanel();
    var authFieldPanel = UI.PanelFactory.panel(useActiveDirectoryAuthenticationCheckBox)
        .resizeX(false)
        .createPanel();
    azureRelatedFieldsPanel.setLayout(new BoxLayout(azureRelatedFieldsPanel, BoxLayout.Y_AXIS));
    azureRelatedFieldsPanel.setBorder(JBUI.Borders.empty(8, 16, 0, 0));

    azureRelatedFieldsPanel.add(resourceNameFieldPanel);
    azureRelatedFieldsPanel.add(deploymentIdFieldPanel);
    azureRelatedFieldsPanel.add(apiVersionFieldPanel);
    azureRelatedFieldsPanel.add(authFieldPanel);
    SwingUtils.setEqualLabelWidths(deploymentIdFieldPanel, resourceNameFieldPanel);
    SwingUtils.setEqualLabelWidths(apiVersionFieldPanel, resourceNameFieldPanel);

    azureRelatedFieldsPanel.setVisible(SettingsState.getInstance().useAzureService);

    return azureRelatedFieldsPanel;
  }

  private void registerServiceButtons(
      JPanel azureRelatedFieldsPanel, JPanel organizationFieldPanel) {
    var serviceButtonGroup = new ButtonGroup();
    serviceButtonGroup.add(useOpenAIServiceRadioButton);
    serviceButtonGroup.add(useAzureServiceRadioButton);
    useOpenAIServiceRadioButton.addActionListener(e -> {
      azureRelatedFieldsPanel.setVisible(false);
      organizationFieldPanel.setVisible(true);
    });
    useAzureServiceRadioButton.addActionListener(e -> {
      azureRelatedFieldsPanel.setVisible(true);
      organizationFieldPanel.setVisible(false);
    });
  }

  private void registerCompletionButtons() {
    var completionButtonGroup = new ButtonGroup();
    completionButtonGroup.add(useChatCompletionRadioButton);
    completionButtonGroup.add(useTextCompletionRadioButton);
    useChatCompletionRadioButton.addActionListener(e -> {
      chatCompletionBaseModelComboBox.setEnabled(true);
      textCompletionBaseModelComboBox.setEnabled(false);
    });
    useTextCompletionRadioButton.addActionListener(e -> {
      chatCompletionBaseModelComboBox.setEnabled(false);
      textCompletionBaseModelComboBox.setEnabled(true);
    });
  }


  private void registerFields() {
    chatCompletionBaseModelComboBox.setEnabled(useChatCompletionRadioButton.isSelected());
    textCompletionBaseModelComboBox.setEnabled(useTextCompletionRadioButton.isSelected());
  }

  private void handleHyperlinkClicked(HyperlinkEvent event) {
    if (HyperlinkEvent.EventType.ACTIVATED.equals(event.getEventType())) {
      if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
        try {
          Desktop.getDesktop().browse(event.getURL().toURI());
        } catch (IOException | URISyntaxException e) {
          throw new RuntimeException("Couldn't open the browser.", e);
        }
      }
    }
  }
}
