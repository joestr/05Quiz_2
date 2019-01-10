/**
 * Sample Skeleton for 'FXML_Main.fxml' Controller Class
 */
package eu.gstd.quiz.pkgController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.gstd.quiz.pkgData.Answer;
import eu.gstd.quiz.pkgData.JSON.JSONAnswer;
import eu.gstd.quiz.pkgData.JSON.JSONQuestion;
import eu.gstd.quiz.pkgData.JSON.JSONQuiz;
import eu.gstd.quiz.pkgData.Question;
import eu.gstd.quiz.pkgData.Quiz;
import eu.gstd.quiz.pkgData.TripleStringed;
import eu.gstd.quiz.pkgData.TripleStringedImplementation;
import eu.gstd.quiz.pkgMisc.AlertManager;
import eu.gstd.quiz.pkgMisc.ImageProvider;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import xyz.joestr.databasex.DatabaseConnectionHandler;
import xyz.joestr.databasex.DatabaseWrapper;

public class FXML_MainController {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="labelMessage"
    private Label labelMessage; // Value injected by FXMLLoader

    @FXML // fx:id="treeTableViewQuizzes"
    private TreeTableView<TripleStringed> treeTableView; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarQuiz"
    private MenuBar menuBarQuiz; // Value injected by FXMLLoader

    @FXML // fx:id="menuFile"
    private Menu menuFile; // Value injected by FXMLLoader

    @FXML // fx:id="menuItemSetDatabase"
    private MenuItem menuItemSetDatabase; // Value injected by FXMLLoader

    @FXML
    private MenuItem menuItemFileEntriesToJSON;

    @FXML
    private MenuItem menuItemFileEntryToJSON;

    @FXML
    private Menu menuEntries;

    @FXML
    private MenuItem menuItemEntriesReload;

    @FXML
    private Menu menuHelp;

    @FXML
    private MenuItem menuItemHelpAbout;

    @FXML // fx:id="treeTableColumnId"
    private TreeTableColumn<TripleStringed, String> treeTableColumnId; // Value injected by FXMLLoader

    @FXML // fx:id="treeTableColumnText"
    private TreeTableColumn<TripleStringed, String> treeTableColumnText; // Value injected by FXMLLoader

    @FXML
    void onEditCommitTreeTableColumnId(ActionEvent event) {

    }

    @FXML
    void onEditCommitTreeTableColumnText(TreeTableColumn.CellEditEvent<TripleStringed, String> event) {

        // Check if a quiz was selected
        if (event.getRowValue().getValue() instanceof Quiz) {

            // Create two quiz instances to the databasw wrapper
            Quiz oldQuiz = (Quiz) event.getRowValue().getValue();
            Quiz newQuiz = new Quiz(oldQuiz.getId(), event.getNewValue());

            // Perform asynchroniously
            CompletableFuture.supplyAsync(() -> {
                int result = 0;

                try {
                    result = this.databaseQuizWrapper.update(oldQuiz, newQuiz);
                } catch (Exception e) {
                    // An exception has to be wrapped in a completion exception
                    throw new CompletionException(e);
                }

                return result;
            }
            ).whenComplete((result, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst updating text from quiz!"
                        );
                        alert.show();
                    });
                    return;
                }

                // Update the quiz
                if (result == 1) {
                    ((Quiz) event.getRowValue().getValue()).setDescription(event.getNewValue());
                }
            }
            );

        }

        if (event.getRowValue().getValue() instanceof Question) {

            Question oldQuestion = (Question) event.getRowValue().getValue();
            Question newQuestion = new Question(oldQuestion.getQuizId(), oldQuestion.getId(), event.getNewValue(), oldQuestion.getCorrectAnswer());

            CompletableFuture.supplyAsync(() -> {
                int result = 0;

                try {
                    result = this.databaseQuestionWrapper.update(oldQuestion, newQuestion);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }

                return result;
            }
            ).whenComplete((result, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst updating text from question!"
                        );
                        alert.show();
                    });
                    return;
                }

                if (result == 1) {
                    ((Question) event.getRowValue().getValue()).setText(event.getNewValue());
                }
            }
            );
        }

        if (event.getRowValue().getValue() instanceof Answer) {

            Answer oldAnswer = (Answer) event.getRowValue().getValue();
            Answer newAnswer = new Answer(oldAnswer.getQuizId(), oldAnswer.getQuestionId(), oldAnswer.getId(), event.getNewValue());

            CompletableFuture.supplyAsync(() -> {
                int result = 0;

                try {
                    result = this.databaseAnswerWrapper.update(oldAnswer, newAnswer);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }

                return result;
            }
            ).whenComplete((result, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst updating text from answer!"
                        );
                        alert.show();
                    });
                    return;
                }

                if (result == 1) {
                    ((Answer) event.getRowValue().getValue()).setText(event.getNewValue());
                }
            }
            );
        }
    }

    @FXML
    void onMouseClickedQuiz(MouseEvent event) {

    }

    @FXML
    void onSelectMenuFile(ActionEvent event) throws Exception {

        if (event.getSource() == this.menuItemSetDatabase) {

            // Creates some  entries to choose from
            List<String> choices = new ArrayList<>();
            choices.add("192.168.234.223");   // TdoT edit
            //choices.add("192.168.128.152"); // TdoT edit
            //choices.add("212.152.179.117"); // TdoT edit

            // Create a new coice dialog
            //ChoiceDialog<String> dialog = new ChoiceDialog<>("192.168.128.152", choices); // TdoT edit
            ChoiceDialog<String> dialog = new ChoiceDialog<>("192.168.234.223", choices);   // TdoT edit
            dialog.setTitle("Database IP");
            dialog.setHeaderText("Database IP");
            dialog.setContentText("Intern or extern:");

            // Show the dialog and wait
            Optional<String> connectionString = dialog.showAndWait();

            // if a value is present, continue
            connectionString.ifPresent(value -> {
                this.connectionString = this.connectionString.replace("%database_ip%", value);
            }
            );

            this.labelMessage.setText("Please be paitient whilst validating connection!");

            // Checking the connection async
            CompletableFuture.supplyAsync(() -> {
                this.databaseConnectionHandler = new DatabaseConnectionHandler(this.connectionString);

                try {
                    this.databaseConnectionHandler.connect();

                    this.databaseQuizWrapper = new DatabaseWrapper<>(Quiz.class, this.databaseConnectionHandler);
                    this.databaseQuestionWrapper = new DatabaseWrapper<>(Question.class, this.databaseConnectionHandler);
                    this.databaseAnswerWrapper = new DatabaseWrapper<>(Answer.class, this.databaseConnectionHandler);

                } catch (Exception e) {
                    // Wrap the original exception
                    throw new CompletionException(e);
                }
                return true;
            }).whenComplete((result, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst validating database connection!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    this.menuItemFileEntriesToJSON.disableProperty().setValue(Boolean.FALSE);
                    this.menuItemFileEntryToJSON.disableProperty().setValue(Boolean.FALSE);

                    this.menuItemEntriesReload.disableProperty().setValue(Boolean.FALSE);

                    this.treeTableView.showRootProperty().setValue(Boolean.TRUE);

                    this.labelMessage.setText("Validated database connection!");
                });
            });
        }

        if (event.getSource() == this.menuItemFileEntriesToJSON) {

            // New file chooser
            FileChooser fileChooser = new FileChooser();

            // limit to json files
            fileChooser.setTitle("Export entries to JSON (from database)");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JSON File", "*.json")
            );

            File selectedFile = fileChooser.showSaveDialog(contextMenu);

            if (selectedFile == null) {
                return;
            }

            // Set the connection persitent, so a little bit faster
            this.databaseConnectionHandler.setPersistentConnection(true);

            List<JSONQuiz> quizzes = new ArrayList<>();

            // run this whole mess async, (it takes really some time to generate this)
            CompletableFuture.supplyAsync(() -> {
                try {
                    for (Quiz q : this.databaseQuizWrapper.select()) {

                        JSONQuiz jsonQuiz = new JSONQuiz(q.getId(), q.getDescription());

                        for (Question qq : this.databaseQuestionWrapper.select("tid = '" + q.getId() + "'")) {

                            JSONQuestion jsonQuestion = new JSONQuestion(qq.getId(), qq.getText());

                            for (Answer a : this.databaseAnswerWrapper.select("tid = '" + q.getId() + "' AND fnr = " + qq.getId())) {

                                JSONAnswer jsonAnswer = new JSONAnswer(a.getId(), a.getText(), false);

                                if (a.getId().equals(qq.getCorrectAnswer())) {

                                    jsonAnswer.setCorrect(true);
                                }

                                jsonQuestion.getAnswers().add(jsonAnswer);
                            }

                            jsonQuiz.getQuestions().add(jsonQuestion);
                        }

                        quizzes.add(jsonQuiz);
                    }

                    // Write to the file with some Gson stuff before
                    Writer osWriter = new OutputStreamWriter(new FileOutputStream(selectedFile));
                    Gson gson = new GsonBuilder()
                        .enableComplexMapKeySerialization()
                        .setPrettyPrinting()
                        .create();
                    gson.toJson(quizzes, osWriter);
                    osWriter.close();
                } catch (Exception e) {
                    // Wraping christmas presents, ummm Completion Exception for Christmas?!
                    throw new CompletionException(e);
                }

                return true;
            }
            ).whenComplete((result, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst exporting to JSON file!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    labelMessage.setText("Exported to '" + selectedFile.getName() + "'!");
                }
                );
            }
            );

            this.databaseConnectionHandler.setPersistentConnection(false);

            this.labelMessage.setText("Please be paitient while exporting to '" + selectedFile.getName() + "'!");
        }

        if (event.getSource() == this.menuItemFileEntryToJSON) {

            // If the user had not selected anything to export -> nothing *woosh*
            if (this.treeTableView.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            // Another file chooser
            FileChooser fileChooser = new FileChooser();

            // only json
            fileChooser.setTitle("Export entry (from local)");
            fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("JSON File", "*.json")
            );

            File selectedFile = fileChooser.showSaveDialog(contextMenu);

            if (selectedFile == null) {
                return;
            }

            // store some usefull variables
            TreeItem<TripleStringed> targetNode = this.treeTableView.getSelectionModel().getSelectedItem();
            TripleStringed targetObject = this.treeTableView.getSelectionModel().getSelectedItem().getValue();

            // if it is a quiz
            if (targetObject instanceof Quiz) {

                JSONQuiz jsonQuiz = new JSONQuiz(((Quiz) targetObject).getId(), ((Quiz) targetObject).getDescription());

                // async (you know)
                CompletableFuture.supplyAsync(() -> {
                    try {
                        for (TreeItem<TripleStringed> qq : targetNode.getChildren()) {

                            JSONQuestion jsonQuestion = new JSONQuestion(((Question) qq.getValue()).getId(), ((Question) qq.getValue()).getText());

                            for (TreeItem<TripleStringed> a : qq.getChildren()) {

                                JSONAnswer jsonAnswer = new JSONAnswer(((Answer) a.getValue()).getId(), ((Answer) a.getValue()).getText(), false);

                                if (((Answer) a.getValue()).getId().equals(((Question) qq.getValue()).getCorrectAnswer())) {

                                    jsonAnswer.setCorrect(true);
                                }

                                jsonQuestion.getAnswers().add(jsonAnswer);
                            }

                            jsonQuiz.getQuestions().add(jsonQuestion);
                        }

                        // gson things
                        Writer osWriter = new OutputStreamWriter(new FileOutputStream(selectedFile));
                        Gson gson = new GsonBuilder()
                            .enableComplexMapKeySerialization()
                            .setPrettyPrinting()
                            .create();
                        gson.toJson(jsonQuiz, osWriter);
                        osWriter.close();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    return true;
                }).whenComplete((result, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst exporting to JSON file!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    Platform.runLater(() -> {
                        labelMessage.setText("Exported to '" + selectedFile.getName() + "'!");
                    });
                });
            }

            // question?
            if (targetObject instanceof Question) {

                JSONQuestion jsonQuestion = new JSONQuestion(((Question) targetObject).getId(), ((Question) targetObject).getText());

                // fancy async
                CompletableFuture.supplyAsync(() -> {
                    try {
                        for (TreeItem<TripleStringed> a : targetNode.getChildren()) {

                            JSONAnswer jsonAnswer = new JSONAnswer(((Answer) a.getValue()).getId(), ((Answer) a.getValue()).getText(), false);

                            if (((Answer) a.getValue()).getId().equals(((Question) targetNode.getValue()).getCorrectAnswer())) {

                                jsonAnswer.setCorrect(true);
                            }

                            jsonQuestion.getAnswers().add(jsonAnswer);
                        }

                        // gson stuff
                        Writer osWriter = new OutputStreamWriter(new FileOutputStream(selectedFile));
                        Gson gson = new GsonBuilder()
                            .enableComplexMapKeySerialization()
                            .setPrettyPrinting()
                            .create();
                        gson.toJson(jsonQuestion, osWriter);
                        osWriter.close();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    return true;
                }).whenComplete((result, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst exporting to JSON file!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    Platform.runLater(() -> {
                        labelMessage.setText("Exported to '" + selectedFile.getName() + "'!");
                    });
                });
            }

            // answer!
            if (targetObject instanceof Answer) {
                JSONAnswer jsonAnswer = new JSONAnswer(((Answer) targetObject).getId(), ((Answer) targetObject).getText(), false);

                // tiny async (maybe not usefull here)
                CompletableFuture.supplyAsync(() -> {
                    try {
                        if (((Answer) targetNode.getValue()).getId().equals(((Question) targetNode.getParent().getValue()).getCorrectAnswer())) {

                            jsonAnswer.setCorrect(true);
                        }

                        // another gson
                        Writer osWriter = new OutputStreamWriter(new FileOutputStream(selectedFile));
                        Gson gson = new GsonBuilder()
                            .enableComplexMapKeySerialization()
                            .setPrettyPrinting()
                            .create();
                        gson.toJson(jsonAnswer, osWriter);
                        osWriter.close();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }

                    return true;
                }).whenComplete((result, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst exporting to JSON file!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    Platform.runLater(() -> {
                        labelMessage.setText("Exported to '" + selectedFile.getName() + "'!");
                    });
                });
            }
        }
    }

    @FXML
    void onSelectMenuHelp(ActionEvent event) {

        if (event.getSource() == this.menuItemHelpAbout) {

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("About");
            alert.setContentText(
                "QuizManager v0.1.0\n"
                + "Copyright (c) 2018 Joel Strasser (joestr)\n"
                + "\n"
                + "Open source libraries used:\n"
                + "DatabaseX v0.1.1\n"
                + "Copyright (c) 2018 Joel Strasser (joestr)"
            );

            alert.showAndWait();
        }
    }

    @FXML
    void onSelectMenuEntries(ActionEvent event) {

        if (event.getSource() == this.menuItemEntriesReload) {
            this.labelMessage.setText("Please be paitent while the tree builds!");

            // clear the root element, so we don't add items till a stackoverflow
            this.treeTableView.getRoot().getChildren().clear();

            // faster connection
            this.databaseConnectionHandler.setPersistentConnection(true);

            // async tree building (a real mess, but it works!)
            CompletableFuture.supplyAsync(() -> {
                try {
                    for (Quiz q : this.databaseQuizWrapper.select()) {

                        TreeItem<TripleStringed> tq = new TreeItem<>(q, this.imageProvider.getPapirus_icon_theme_emblem_generic());
                        this.rootElement.getChildren().add(tq);

                        for (Question qq : this.databaseQuestionWrapper.select("tid = '" + q.getId() + "'")) {

                            TreeItem<TripleStringed> tqq = new TreeItem<>(qq, this.imageProvider.getPapirus_icon_theme_emblem_question());
                            tq.getChildren().add(tqq);

                            for (Answer a : this.databaseAnswerWrapper.select("tid = '" + q.getId() + "' AND fnr = " + qq.getId())) {
                                TreeItem<TripleStringed> ta
                                    = new TreeItem<>(
                                        a,
                                        a.getId().equals(qq.getCorrectAnswer())
                                        ? this.imageProvider.getPapirus_icon_theme_emblem_default()
                                        : this.imageProvider.getPapirus_icon_theme_emblem_unreadable()
                                    );
                                tqq.getChildren().add(ta);
                            }
                        }
                    }
                } catch (Exception e) {
                    // wraping in a completion exception
                    throw new CompletionException(e);
                }
                return true;
            }).whenComplete((result, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst building tree!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    labelMessage.setText("Built tree!");
                });
            });

            this.databaseConnectionHandler.setPersistentConnection(false);
        }
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws InstantiationException, IllegalAccessException, IOException {

        // Menu entries
        this.menuItemFileEntriesToJSON.disableProperty().setValue(Boolean.TRUE);
        this.menuItemFileEntryToJSON.disableProperty().setValue(Boolean.TRUE);

        this.menuItemEntriesReload.disableProperty().setValue(Boolean.TRUE);

        // Treetable stuff
        this.treeTableColumnId.setCellValueFactory(new TreeItemPropertyValueFactory<>("string1"));
        this.treeTableColumnText.setCellValueFactory(new TreeItemPropertyValueFactory<>("string2"));

        this.treeTableColumnText.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());

        this.treeTableView.getColumns().setAll(this.treeTableColumnId, this.treeTableColumnText);

        // fancy icons!
        this.imageProvider = new ImageProvider();

        // dedicated root element
        rootElement = new TreeItem<>(new TripleStringedImplementation("ROOT", "ROOT", "ROOT"));

        // some settings
        this.treeTableView.setRoot(rootElement);
        this.treeTableView.getRoot().setExpanded(true);
        this.treeTableView.showRootProperty().setValue(Boolean.FALSE);
        this.treeTableView.setEditable(true);

        //NetBeans - collapsed this (no comments, trust me!)
        //<editor-fold defaultstate="collapsed" desc="addQuizFromRoot#setOnAction">
        this.addQuizFromRoot.setOnAction((event) -> {
            // na, comments here

            this.labelMessage.setText("Please be paitient whilst adding quiz!");

            // new input box
            TextInputDialog dialog = new TextInputDialog("Quiz ID");
            dialog.setTitle("Quiz ID");
            dialog.setHeaderText("Quiz ID");
            dialog.setContentText("Please enter quiz ID:");

            Optional<String> result = dialog.showAndWait();

            // if the name is present -> execute async
            result.ifPresent(name -> {
                // fairly simple
                CompletableFuture.supplyAsync(() -> {
                    Quiz quizToAdd = new Quiz(name, "Quiz " + name);
                    try {
                        databaseQuizWrapper.insert(quizToAdd);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return quizToAdd;
                }
                ).whenComplete((mayResult, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst adding quiz!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    Platform.runLater(() -> {
                        // add as child to the root elemnt
                        rootElement.getChildren().add(new TreeItem<>(
                            mayResult,
                            imageProvider.getPapirus_icon_theme_emblem_generic()
                        )
                        );
                        // just for ... i don't know
                        treeTableView.refresh();

                        labelMessage.setText("Added quiz '" + name + "'!");
                    });
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="addQuizFromQuiz#setOnAction">
        this.addQuizFromQuiz.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst adding quiz!");

            // new input box
            TextInputDialog dialog = new TextInputDialog("Quiz ID");
            dialog.setTitle("Quiz ID");
            dialog.setHeaderText("Quiz ID");
            dialog.setContentText("Please enter quiz ID:");

            Optional<String> result = dialog.showAndWait();

            // if the name is present -> execute async
            result.ifPresent(name -> {
                // fairly simple
                CompletableFuture.supplyAsync(() -> {
                    Quiz quizToAdd = new Quiz(name, "Quiz " + name);
                    try {
                        databaseQuizWrapper.insert(quizToAdd);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return quizToAdd;
                }
                ).whenComplete((mayResult, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst adding quiz!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    Platform.runLater(() -> {
                        // add as child to the root elemnt
                        rootElement.getChildren().add(new TreeItem<>(
                            mayResult,
                            imageProvider.getPapirus_icon_theme_emblem_generic()
                        )
                        );
                        // just for ... i don't know
                        treeTableView.refresh();

                        labelMessage.setText("Added quiz '" + name + "'!");
                    });
                }
                );
            }
            );
        }
        );
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="deleteQuizFromQuiz#setOnAction">
        this.deleteQuizFromQuiz.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst deleting quiz!");

            // some variables to work with
            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Quiz targetObject = (Quiz) treeTableView.getSelectionModel().getSelectedItem().getValue();

            // async this stuff (this  is really heavy to perform)
            CompletableFuture.supplyAsync(() -> {
                try {
                    // persitent connection
                    databaseConnectionHandler.setPersistentConnection(true);
                    databaseConnectionHandler.connect();

                    // disbale foreign key
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE frage DISABLE CONSTRAINT fk_antwort_ok"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE antwort DISABLE CONSTRAINT fkantwort_frage"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE teilnahme DISABLE CONSTRAINT fkteilnahme_test"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort DISABLE CONSTRAINT fktestantwort_antwort"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort DISABLE CONSTRAINT fktestantwort_teilnahme"
                    );
                    databaseConnectionHandler.getConnection().createStatement().executeUpdate(
                        "DELETE FROM testantwort WHERE tid = '" + targetObject.getId() + "'"
                    );
                    databaseConnectionHandler.getConnection().createStatement().executeUpdate(
                        "DELETE FROM teilnahme WHERE tid = '" + targetObject.getId() + "'"
                    );

                    // loop through the questions
                    for (TreeItem<TripleStringed> questionNode : targetNode.getChildren()) {

                        // loop through the answers
                        for (TreeItem<TripleStringed> answerNode : questionNode.getChildren()) {
                            Answer answerToDelete = (Answer) answerNode.getValue();
                            databaseAnswerWrapper.delete(answerToDelete);
                        }

                        Question questionToDelete = (Question) questionNode.getValue();
                        databaseQuestionWrapper.delete(questionToDelete);
                    }

                    // finally delete the quiz
                    databaseQuizWrapper.delete(targetObject);

                    // turn constraints back on
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE frage ENABLE CONSTRAINT fk_antwort_ok"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE antwort ENABLE CONSTRAINT fkantwort_frage"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE teilnahme ENABLE CONSTRAINT fkteilnahme_test"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort ENABLE CONSTRAINT fktestantwort_antwort"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort ENABLE CONSTRAINT fktestantwort_teilnahme"
                    );
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
                return true;
            }
            ).whenComplete((mayResult, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst deleting quiz!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    // remove quiz from the root
                    treeTableView.getRoot().getChildren().remove(targetNode);
                    treeTableView.refresh();

                    labelMessage.setText("Deleted quiz '" + targetObject.getId() + "'!");
                });
            }
            );
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="addQuestionFromQuiz#setOnAction">
        this.addQuestionFromQuiz.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst adding question!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Quiz targetObject = (Quiz) treeTableView.getSelectionModel().getSelectedItem().getValue();

            TextInputDialog dialog = new TextInputDialog("Question text");
            dialog.setTitle("Question text");
            dialog.setHeaderText("Question text");
            dialog.setContentText("Please enter question text:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                CompletableFuture.supplyAsync(() -> {
                    List<TripleStringed> r = new ArrayList<>();

                    try {
                        databaseConnectionHandler.setPersistentConnection(true);
                        databaseConnectionHandler.connect();

                        int nextId = 0;
                        ResultSet rs = databaseConnectionHandler.getConnection().createStatement().executeQuery("SELECT NVL(MAX(fnr), 0) + 1 FROM frage WHERE tid = '" + targetObject.getId() + "'");
                        rs.next();
                        nextId = rs.getInt(1);

                        Question questionToAdd = new Question(targetObject.getId(), nextId, name, 1);
                        Answer answerToAdd = new Answer(targetObject.getId(), questionToAdd.getId(), 1, "Richtige antwort");
                        r.add(questionToAdd);
                        r.add(answerToAdd);

                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE frage DISABLE CONSTRAINT fk_antwort_ok"
                        );
                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE antwort DISABLE CONSTRAINT fkantwort_frage"
                        );

                        databaseQuestionWrapper.insert(questionToAdd);
                        databaseAnswerWrapper.insert(answerToAdd);

                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE frage ENABLE CONSTRAINT fk_antwort_ok"
                        );
                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE antwort ENABLE CONSTRAINT fkantwort_frage"
                        );

                        databaseConnectionHandler.disconnect();
                        databaseConnectionHandler.setPersistentConnection(false);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return r;
                }).whenComplete((mayResult, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst adding question!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    if (mayResult.isEmpty()) {
                        return;
                    }

                    Platform.runLater(() -> {
                        TreeItem<TripleStringed> addedQuestion = new TreeItem(mayResult.get(0), imageProvider.getPapirus_icon_theme_emblem_question());
                        addedQuestion.getChildren().add(new TreeItem<>(mayResult.get(1), imageProvider.getPapirus_icon_theme_emblem_default()));

                        targetNode.getChildren().addAll(addedQuestion);
                        treeTableView.refresh();

                        labelMessage.setText(
                            "Added question '" + mayResult.get(0).getString1()
                            + "' to quiz '" + mayResult.get(1).getString1()
                            + "'!"
                        );
                    });
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="deleteQuestionFromQuestion#setOnAction">
        this.deleteQuestionFromQuestion.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst deleting question!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Question targetObject = (Question) treeTableView.getSelectionModel().getSelectedItem().getValue();

            CompletableFuture.supplyAsync(() -> {
                try {
                    databaseConnectionHandler.setPersistentConnection(true);
                    databaseConnectionHandler.connect();

                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE frage DISABLE CONSTRAINT fk_antwort_ok"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE antwort DISABLE CONSTRAINT fkantwort_frage"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort DISABLE CONSTRAINT fktestantwort_antwort"
                    );

                    databaseConnectionHandler.getConnection().createStatement().executeUpdate(
                        "DELETE FROM testantwort WHERE tid = '" + targetObject.getQuizId() + "' AND fnr = " + targetObject.getId()
                    );

                    for (TreeItem<TripleStringed> answerNode : targetNode.getChildren()) {

                        Answer answerToDelete = (Answer) answerNode.getValue();
                        databaseAnswerWrapper.delete(answerToDelete);
                    }

                    databaseQuestionWrapper.delete(targetObject);

                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE frage ENABLE CONSTRAINT fk_antwort_ok"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE antwort ENABLE CONSTRAINT fkantwort_frage"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort ENABLE CONSTRAINT fktestantwort_antwort"
                    );
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
                return true;
            }).whenComplete((mayResult, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst deleting question!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    targetNode.getParent().getChildren().removeAll(targetNode);
                    treeTableView.refresh();

                    labelMessage.setText(
                        "Deleted question '" + targetObject.getId()
                        + "' from quiz '" + targetObject.getId()
                        + "'!"
                    );
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="addQuestionFromQuestion#setOnAction">
        this.addQuestionFromQuestion.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst adding question!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Question targetObject = (Question) treeTableView.getSelectionModel().getSelectedItem().getValue();

            TextInputDialog dialog = new TextInputDialog("Question text");
            dialog.setTitle("Question text");
            dialog.setHeaderText("Question text");
            dialog.setContentText("Please enter Question text:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                CompletableFuture.supplyAsync(() -> {
                    List<TripleStringed> r = new ArrayList<>();
                    try {
                        databaseConnectionHandler.setPersistentConnection(true);
                        databaseConnectionHandler.connect();

                        int nextId = 0;
                        ResultSet rs = databaseConnectionHandler.getConnection().createStatement().executeQuery("SELECT NVL(MAX(fnr), 0) + 1 AS FROM antwort WHERE tid = '" + targetObject.getQuizId() + "'");
                        rs.next();
                        nextId = rs.getInt(1);

                        Question questionToAdd = new Question(targetObject.getQuizId(), nextId, name, 1);
                        Answer answerToAdd = new Answer(targetObject.getQuizId(), questionToAdd.getId(), 1, "Correct answer for " + name);
                        r.add(questionToAdd);
                        r.add(answerToAdd);

                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE frage DISABLE CONSTRAINT fk_antwort_ok"
                        );
                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE antwort DISABLE CONSTRAINT fkantwort_frage"
                        );

                        databaseQuestionWrapper.insert(questionToAdd);
                        databaseAnswerWrapper.insert(answerToAdd);

                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE frage ENABLE CONSTRAINT fk_antwort_ok"
                        );
                        databaseConnectionHandler.getConnection().createStatement().execute(
                            "ALTER TABLE antwort ENABLE CONSTRAINT fkantwort_frage"
                        );

                        databaseConnectionHandler.disconnect();
                        databaseConnectionHandler.setPersistentConnection(false);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return r;
                }).whenComplete((mayResult, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst adding question!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    if (mayResult.isEmpty()) {
                        return;
                    }

                    Platform.runLater(() -> {
                        TreeItem<TripleStringed> addedQuestion = new TreeItem(mayResult.get(0), imageProvider.getPapirus_icon_theme_emblem_question());
                        addedQuestion.getChildren().add(new TreeItem<>(mayResult.get(1), imageProvider.getPapirus_icon_theme_emblem_default()));

                        targetNode.getParent().getChildren().addAll(addedQuestion);

                        treeTableView.refresh();

                        labelMessage.setText(
                            "Added question '" + mayResult.get(0).getString1()
                            + "' to quiz '" + mayResult.get(1).getString1()
                            + "'!"
                        );
                    });
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="addAnswerFromQuestion#setOnAction">
        this.addAnswerFromQuestion.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst adding answer!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Question targetObject = (Question) treeTableView.getSelectionModel().getSelectedItem().getValue();

            TextInputDialog dialog = new TextInputDialog("Answer text");
            dialog.setTitle("Answer text");
            dialog.setHeaderText("Answer text");
            dialog.setContentText("Please enter answer text:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                CompletableFuture.supplyAsync(() -> {
                    Answer answerToAdd = null;
                    try {
                        databaseConnectionHandler.connect();
                        int nextId = 0;
                        ResultSet rs = databaseConnectionHandler.getConnection().createStatement().executeQuery(
                            "SELECT NVL(MAX(anr), 0) + 1 FROM antwort WHERE tid = '" + targetObject.getQuizId()
                            + "' AND fnr = " + targetObject.getId()
                        );
                        rs.next();
                        nextId = rs.getInt(1);
                        answerToAdd = new Answer(
                            targetObject.getQuizId(),
                            targetObject.getId(),
                            nextId,
                            name
                        );
                        databaseAnswerWrapper.insert(answerToAdd);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return answerToAdd;
                }).whenComplete((mayResult, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst adding answer!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    if (mayResult == null) {
                        return;
                    }

                    Platform.runLater(() -> {
                        targetNode.getChildren().add(new TreeItem<>(mayResult, imageProvider.getPapirus_icon_theme_emblem_unreadable()));
                        treeTableView.refresh();

                        labelMessage.setText(
                            "Added answer '" + mayResult.getId()
                            + "' to question '" + mayResult.getQuestionId()
                            + "' from quiz '" + mayResult.getQuizId()
                            + "'!"
                        );
                    });
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="addAnswerFromAnswer#setOnAction">
        this.addAnswerFromAnswer.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst adding answer!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Answer targetObject = (Answer) treeTableView.getSelectionModel().getSelectedItem().getValue();

            TextInputDialog dialog = new TextInputDialog("Answer text");
            dialog.setTitle("Answer text");
            dialog.setHeaderText("Answer text");
            dialog.setContentText("Please enter answer text:");

            Optional<String> result = dialog.showAndWait();

            result.ifPresent(name -> {
                CompletableFuture.supplyAsync(() -> {
                    Answer answerToAdd = null;
                    try {
                        databaseConnectionHandler.connect();
                        int nextId = 0;
                        ResultSet rs = databaseConnectionHandler.getConnection().createStatement().executeQuery(
                            "SELECT NVL(MAX(anr), 0) + 1 FROM antwort WHERE tid = '" + targetObject.getQuizId()
                            + "' AND fnr = " + targetObject.getQuestionId()
                        );
                        rs.next();
                        nextId = rs.getInt(1);
                        answerToAdd = new Answer(
                            targetObject.getQuizId(),
                            targetObject.getId(),
                            nextId,
                            name
                        );
                        databaseAnswerWrapper.insert(answerToAdd);
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                    return answerToAdd;
                }).whenComplete((mayResult, exception) -> {
                    if (exception != null) {
                        Platform.runLater(() -> {
                            labelMessage.setText("");
                            Alert alert = AlertManager.exception(
                                exception.getCause(),
                                "Error occoured",
                                "Error whilst adding answer!"
                            );
                            alert.show();
                        });
                        return;
                    }

                    if (mayResult == null) {
                        return;
                    }

                    Platform.runLater(() -> {
                        targetNode.getParent().getChildren().add(new TreeItem<>(mayResult, imageProvider.getPapirus_icon_theme_emblem_unreadable()));
                        treeTableView.refresh();

                        labelMessage.setText(
                            "Added answer '" + targetObject.getId()
                            + "' to question '" + targetObject.getQuestionId()
                            + "' from quiz '" + targetObject.getQuizId()
                            + "'!"
                        );
                    });
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="deleteAnswerFromAnswer#setOnAction">
        this.deleteAnswerFromAnswer.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst deleting answer!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Answer targetObject = (Answer) treeTableView.getSelectionModel().getSelectedItem().getValue();

            CompletableFuture.supplyAsync(() -> {
                int result = -1;
                try {
                    databaseConnectionHandler.setPersistentConnection(true);
                    databaseConnectionHandler.connect();

                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE frage DISABLE CONSTRAINT fk_antwort_ok"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE antwort DISABLE CONSTRAINT fkantwort_frage"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort DISABLE CONSTRAINT fktestantwort_antwort"
                    );

                    databaseConnectionHandler.getConnection().createStatement().executeUpdate(
                        "DELETE FROM testantwort WHERE tid = '" + targetObject.getQuizId() + "' AND fnr = " + targetObject.getQuestionId() + " AND anr = " + targetObject.getId()
                    );

                    if (((Question) targetNode.getParent().getValue()).getCorrectAnswer().equals(targetObject.getId())) {

                        databaseConnectionHandler.getConnection().createStatement().executeUpdate(
                            "DELETE FROM testantwort WHERE tid = '" + targetObject.getQuizId() + "' AND fnr = " + targetObject.getId()
                        );

                        databaseConnectionHandler.getConnection().createStatement().executeUpdate(
                            "DELETE FROM antwort WHERE tid = '" + targetObject.getQuizId() + "' AND fnr = " + targetObject.getId()
                        );

                        databaseQuestionWrapper.delete((Question) targetNode.getParent().getValue());

                        databaseAnswerWrapper.delete(targetObject);

                        result = 2;
                    } else {

                        databaseAnswerWrapper.delete(targetObject);
                        result = 1;
                    }

                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE frage ENABLE CONSTRAINT fk_antwort_ok"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE antwort ENABLE CONSTRAINT fkantwort_frage"
                    );
                    databaseConnectionHandler.getConnection().createStatement().execute(
                        "ALTER TABLE testantwort ENABLE CONSTRAINT fktestantwort_antwort"
                    );
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
                return result;
            }).whenComplete((mayResult, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst deleting answer!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    // normal
                    if (mayResult == 1) {
                        targetNode.getParent().getChildren().remove(targetNode);
                    }

                    // deleted question too
                    if (mayResult == 2) {
                        targetNode.getParent().getParent().getChildren().remove(targetNode.getParent());
                    }

                    treeTableView.refresh();

                    labelMessage.setText(
                        "Deleted answer '" + targetObject.getId()
                        + "' from question " + targetObject.getQuestionId()
                        + "' of quiz '" + targetObject.getQuizId()
                        + "'!"
                    );
                });
            });
        });
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="marksAsCorrectAnswerFromAnswer#setOnAction">
        this.markAsCorrectAnswerFromAnswer.setOnAction((event) -> {
            this.labelMessage.setText("Please be paitient whilst marking answer as correct!");

            TreeItem<TripleStringed> targetNode = treeTableView.getSelectionModel().getSelectedItem();
            Answer targetObject = (Answer) treeTableView.getSelectionModel().getSelectedItem().getValue();

            CompletableFuture.supplyAsync(() -> {
                int result = -1;
                try {
                    Question parentQuestion = (Question) targetNode.getParent().getValue();
                    Question oldQuestion = new Question(
                        parentQuestion.getQuizId(),
                        parentQuestion.getId(),
                        parentQuestion.getText(),
                        parentQuestion.getCorrectAnswer()
                    );
                    parentQuestion.setCorrectAnswer(targetObject.getId());

                    databaseQuestionWrapper.update(oldQuestion, parentQuestion);
                } catch (Exception e) {
                    throw new CompletionException(e);
                }
                return result;
            }).whenComplete((mayResult, exception) -> {
                if (exception != null) {
                    Platform.runLater(() -> {
                        labelMessage.setText("");
                        Alert alert = AlertManager.exception(
                            exception.getCause(),
                            "Error occoured",
                            "Error whilst marking answer as correct!"
                        );
                        alert.show();
                    });
                    return;
                }

                Platform.runLater(() -> {
                    targetNode.getParent().getChildren().forEach((item) -> {
                        item.setGraphic(imageProvider.getPapirus_icon_theme_emblem_unreadable());
                    });

                    targetNode.setGraphic(imageProvider.getPapirus_icon_theme_emblem_default());

                    treeTableView.refresh();

                    labelMessage.setText(
                        "Marked answer '" + targetObject.getId()
                        + "' of question '" + targetObject.getQuestionId()
                        + "' of quiz '" + targetObject.getQuizId()
                        + "' as correct!"
                    );
                });
            });
        });
        //</editor-fold>

        // this is a dummy entry, so wenn can disable it
        this.placeholder.setDisable(true);

        // event handler to build up the context menu, which should change depending the entry selected
        this.treeTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, (event) -> {

            if (event.getButton() != MouseButton.SECONDARY) {
                return;
            }

            // nothing selected, nothing to do
            if (treeTableView.getSelectionModel().getSelectedItem() == null) {
                return;
            }

            // set dummy here
            contextMenu.getItems().setAll(placeholder);

            // if its the selected element is the root element
            if (treeTableView.getSelectionModel().getSelectedItem().equals(rootElement)) {
                contextMenu.getItems().setAll(addQuizFromRoot);
            }

            // if it is a quiz then show entries for 'fromQuiz'
            if (treeTableView.getSelectionModel().getSelectedItem().getValue() instanceof Quiz) {
                contextMenu.getItems().setAll(addQuizFromQuiz, deleteQuizFromQuiz, addQuestionFromQuiz);
            }

            // same goes for question
            if (treeTableView.getSelectionModel().getSelectedItem().getValue() instanceof Question) {
                contextMenu.getItems().setAll(addQuestionFromQuestion, deleteQuestionFromQuestion, addAnswerFromQuestion);
            }

            // answer also
            if (treeTableView.getSelectionModel().getSelectedItem().getValue() instanceof Answer) {
                contextMenu.getItems().setAll(addAnswerFromAnswer, deleteAnswerFromAnswer, markAsCorrectAnswerFromAnswer);
            }
        });

        // set the context menu
        this.treeTableView.setContextMenu(contextMenu);
    }

    // Database stuff
    private DatabaseConnectionHandler databaseConnectionHandler;
    private DatabaseWrapper<Quiz> databaseQuizWrapper;
    private DatabaseWrapper<Question> databaseQuestionWrapper;
    private DatabaseWrapper<Answer> databaseAnswerWrapper;

    // the root element
    private TreeItem<TripleStringed> rootElement;

    // the fancy icons
    private ImageProvider imageProvider = new ImageProvider();

    // connection string for database
    //private String connectionString = "jdbc:oracle:thin:d4b23/d4b@%database_ip%:1521:ora11g"; // TdoT edit
    private String connectionString = "jdbc:oracle:thin:quiz/quiz@%database_ip%:1521:ora11g"; // TdoT edit

    // context menu
    private ContextMenu contextMenu = new ContextMenu();

    // ahhh, the menu items
    private final MenuItem addQuizFromRoot = new MenuItem("Add quiz", imageProvider.getPapirus_icon_theme_emblem_generic());

    private final MenuItem addQuizFromQuiz = new MenuItem("Add quiz", imageProvider.getPapirus_icon_theme_emblem_generic());
    private final MenuItem deleteQuizFromQuiz = new MenuItem("Delete quiz");
    private final MenuItem addQuestionFromQuiz = new MenuItem("Add question", imageProvider.getPapirus_icon_theme_emblem_question());

    private final MenuItem addQuestionFromQuestion = new MenuItem("Add question", imageProvider.getPapirus_icon_theme_emblem_question());
    private final MenuItem deleteQuestionFromQuestion = new MenuItem("Delete question");
    private final MenuItem addAnswerFromQuestion = new MenuItem("Add answer", imageProvider.getPapirus_icon_theme_emblem_unreadable());

    private final MenuItem addAnswerFromAnswer = new MenuItem("Add answer", imageProvider.getPapirus_icon_theme_emblem_unreadable());
    private final MenuItem deleteAnswerFromAnswer = new MenuItem("Delete answer");
    private final MenuItem markAsCorrectAnswerFromAnswer = new MenuItem("Mark as correct", imageProvider.getPapirus_icon_theme_emblem_default());

    // dummy menu item
    private final MenuItem placeholder = new MenuItem("Placeholder");
}
