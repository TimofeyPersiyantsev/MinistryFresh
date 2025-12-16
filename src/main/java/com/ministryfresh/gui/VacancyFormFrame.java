package com.ministryfresh.gui;

import com.ministryfresh.models.User;
import com.ministryfresh.models.Vacancy;
import com.ministryfresh.repositories.VacancyRepository;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class VacancyFormFrame extends JFrame {
    private VacancyRepository vacancyRepository;
    private User currentUser;
    private Vacancy vacancyToEdit;

    private JTextField titleField;
    private JTextArea descriptionArea;
    private JTextArea requirementsArea;
    private JTextArea responsibilitiesArea;
    private JTextField salaryField;
    private JTextField locationField;
    private JComboBox<String> employmentTypeCombo;
    private JComboBox<String> experienceLevelCombo;
    private JCheckBox activeCheckBox;
    private JButton saveButton;
    private JButton cancelButton;

    public VacancyFormFrame(VacancyRepository vacancyRepository, User currentUser) {
        this(vacancyRepository, currentUser, null);
    }

    public VacancyFormFrame(VacancyRepository vacancyRepository, User currentUser, Vacancy vacancyToEdit) {
        this.vacancyRepository = vacancyRepository;
        this.currentUser = currentUser;
        this.vacancyToEdit = vacancyToEdit;
        initializeUI();
    }

    private void initializeUI() {
        boolean isEditMode = vacancyToEdit != null;
        setTitle(isEditMode ? "Редактирование вакансии" : "Новая вакансия");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        JLabel titleLabel = new JLabel(isEditMode ? "Редактирование вакансии" : "Создание новой вакансии");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        formPanel.add(new JLabel("Название вакансии:*"));
        titleField = new JTextField(30);
        if (isEditMode) titleField.setText(vacancyToEdit.getTitle());
        formPanel.add(titleField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Описание:*"));
        descriptionArea = new JTextArea(4, 30);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
        if (isEditMode) descriptionArea.setText(vacancyToEdit.getDescription());
        formPanel.add(descriptionScroll);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Требования:"));
        requirementsArea = new JTextArea(3, 30);
        requirementsArea.setLineWrap(true);
        requirementsArea.setWrapStyleWord(true);
        JScrollPane requirementsScroll = new JScrollPane(requirementsArea);
        if (isEditMode && vacancyToEdit.getRequirements() != null)
            requirementsArea.setText(vacancyToEdit.getRequirements());
        formPanel.add(requirementsScroll);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        formPanel.add(new JLabel("Обязанности:"));
        responsibilitiesArea = new JTextArea(3, 30);
        responsibilitiesArea.setLineWrap(true);
        responsibilitiesArea.setWrapStyleWord(true);
        JScrollPane responsibilitiesScroll = new JScrollPane(responsibilitiesArea);
        if (isEditMode && vacancyToEdit.getResponsibilities() != null)
            responsibilitiesArea.setText(vacancyToEdit.getResponsibilities());
        formPanel.add(responsibilitiesScroll);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel rowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel.add(new JLabel("Зарплата:"));
        salaryField = new JTextField(10);
        if (isEditMode && vacancyToEdit.getSalary() != null)
            salaryField.setText(vacancyToEdit.getSalary().toString());
        rowPanel.add(salaryField);

        rowPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        rowPanel.add(new JLabel("Город:"));
        locationField = new JTextField(15);
        if (isEditMode) locationField.setText(vacancyToEdit.getLocation());
        rowPanel.add(locationField);
        formPanel.add(rowPanel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        JPanel rowPanel2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rowPanel2.add(new JLabel("Тип занятости:*"));

        String[] employmentTypes = {
                "Выберите тип",
                "Полная занятость",
                "Частичная занятость",
                "Удаленная работа",
                "Контракт"
        };
        employmentTypeCombo = new JComboBox<>(employmentTypes);
        if (isEditMode) {
            String displayType = vacancyToEdit.getEmploymentTypeDisplay();
            for (int i = 0; i < employmentTypes.length; i++) {
                if (employmentTypes[i].equals(displayType)) {
                    employmentTypeCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        rowPanel2.add(employmentTypeCombo);

        rowPanel2.add(Box.createRigidArea(new Dimension(20, 0)));
        rowPanel2.add(new JLabel("Уровень опыта:*"));

        String[] experienceLevels = {
                "Выберите уровень",
                "Без опыта",
                "Начальный уровень (Junior)",
                "Средний уровень (Middle)",
                "Высокий уровень (Senior)"
        };
        experienceLevelCombo = new JComboBox<>(experienceLevels);
        if (isEditMode) {
            String displayLevel = vacancyToEdit.getExperienceLevelDisplay();
            for (int i = 0; i < experienceLevels.length; i++) {
                if (experienceLevels[i].equals(displayLevel)) {
                    experienceLevelCombo.setSelectedIndex(i);
                    break;
                }
            }
        }
        rowPanel2.add(experienceLevelCombo);
        formPanel.add(rowPanel2);
        formPanel.add(Box.createRigidArea(new Dimension(0, 5)));

        if (isEditMode) {
            activeCheckBox = new JCheckBox("Вакансия активна");
            activeCheckBox.setSelected(vacancyToEdit.isActive());
            formPanel.add(activeCheckBox);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        JPanel buttonPanel = new JPanel(new FlowLayout());
        saveButton = new JButton(isEditMode ? "Сохранить изменения" : "Создать вакансию");
        saveButton.addActionListener(e -> saveVacancy());

        cancelButton = new JButton("Отмена");
        cancelButton.addActionListener(e -> dispose());

        buttonPanel.add(cancelButton);
        buttonPanel.add(saveButton);

        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void saveVacancy() {
        if (titleField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите название вакансии!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (descriptionArea.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Введите описание вакансии!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (employmentTypeCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Выберите тип занятости!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (experienceLevelCombo.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, "Выберите уровень опыта!", "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            BigDecimal salary = null;
            if (!salaryField.getText().trim().isEmpty()) {
                try {
                    salary = new BigDecimal(salaryField.getText().trim());
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Некорректный формат зарплаты!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            String employmentType = "";
            switch ((String) employmentTypeCombo.getSelectedItem()) {
                case "Полная занятость": employmentType = Vacancy.EMPLOYMENT_FULL_TIME; break;
                case "Частичная занятость": employmentType = Vacancy.EMPLOYMENT_PART_TIME; break;
                case "Удаленная работа": employmentType = Vacancy.EMPLOYMENT_REMOTE; break;
                case "Контракт": employmentType = Vacancy.EMPLOYMENT_CONTRACT; break;
            }

            String experienceLevel = "";
            switch ((String) experienceLevelCombo.getSelectedItem()) {
                case "Без опыта": experienceLevel = Vacancy.EXPERIENCE_NO_EXPERIENCE; break;
                case "Начальный уровень (Junior)": experienceLevel = Vacancy.EXPERIENCE_JUNIOR; break;
                case "Средний уровень (Middle)": experienceLevel = Vacancy.EXPERIENCE_MIDDLE; break;
                case "Высокий уровень (Senior)": experienceLevel = Vacancy.EXPERIENCE_SENIOR; break;
            }

            boolean isEditMode = vacancyToEdit != null;

            if (isEditMode) {
                vacancyToEdit.setTitle(titleField.getText().trim());
                vacancyToEdit.setDescription(descriptionArea.getText().trim());
                vacancyToEdit.setRequirements(requirementsArea.getText().trim());
                vacancyToEdit.setResponsibilities(responsibilitiesArea.getText().trim());
                vacancyToEdit.setSalary(salary);
                vacancyToEdit.setLocation(locationField.getText().trim());
                vacancyToEdit.setEmploymentType(employmentType);
                vacancyToEdit.setExperienceLevel(experienceLevel);
                vacancyToEdit.setActive(activeCheckBox.isSelected());

                boolean success = vacancyRepository.updateVacancy(vacancyToEdit);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Вакансия успешно обновлена!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при обновлении вакансии!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                Vacancy newVacancy = new Vacancy(
                        currentUser.getId(),
                        titleField.getText().trim(),
                        descriptionArea.getText().trim(),
                        requirementsArea.getText().trim(),
                        responsibilitiesArea.getText().trim(),
                        salary,
                        locationField.getText().trim(),
                        employmentType,
                        experienceLevel
                );

                int newId = vacancyRepository.createVacancy(newVacancy);

                if (newId > 0) {
                    JOptionPane.showMessageDialog(this, "Вакансия успешно создана!", "Успех", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Ошибка при создании вакансии!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ошибка: " + e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
