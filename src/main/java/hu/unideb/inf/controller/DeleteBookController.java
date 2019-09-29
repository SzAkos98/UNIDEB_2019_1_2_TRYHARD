package hu.unideb.inf.controller;

import hu.unideb.inf.model.Book;
import hu.unideb.inf.utils.DBUtils;
import hu.unideb.inf.utils.DataTypes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static hu.unideb.inf.controller.utils.FXUtils.*;


public class DeleteBookController {
    @FXML
    protected TextField id;

    @FXML
    protected TextField author;

    @FXML
    protected TextField title;

    @FXML
    protected TextField publisher;

    @FXML
    protected TextField yearOfPublication;

    @FXML
    protected TableView<Book> table;

    private Map<TextField, DataTypes> filterFields = new LinkedHashMap<>();

    @FXML
    public void initialize(){
        filterFields.put(id, DataTypes.NUMBER);
        filterFields.put(author, DataTypes.STRING);
        filterFields.put(title, DataTypes.STRING);
        filterFields.put(publisher, DataTypes.STRING);
        filterFields.put(yearOfPublication, DataTypes.NUMBER);
        refreshBookTableView(table, "");
    }

    public void handleSelection(MouseEvent mouseEvent) {
        Book book = table.getSelectionModel().getSelectedItem();
        id.setText(book.getId().toString());
        author.setText(book.getAuthor());
        title.setText(book.getTitle());
        publisher.setText(book.getPublisher());
        yearOfPublication.setText(book.getYearOfPublication().toString());
    }

    public void handleDelete(ActionEvent actionEvent) {

        StringBuilder queryCommand = new StringBuilder("DELETE FROM Book WHERE ");
        String extend;
        int x =0;
        ArrayList<String> prefix = new ArrayList<>();
        prefix.add("id = ");
        prefix.add("author = ");
        prefix.add("title = ");
        prefix.add("publisher = ");
        prefix.add("yearOfPublication = ");

        for(Map.Entry i : filterFields.entrySet()){
            if(textFieldIsEmpty((TextField) i.getKey())){
                x++;
            continue;
            } else {
                extend = wrapValue((TextField) i.getKey(), (DataTypes) i.getValue());
                queryCommand.append(prefix.get(x) + extend + " AND ");
                x++;
            }
        }
        queryCommand.delete(queryCommand.length() - 5, queryCommand.length());
        System.out.println(queryCommand);

        DBUtils.updateTable(queryCommand.toString());

        refreshBookTableView(table, "");
        clearFields();
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        configScene(loadFxml("/fxml/MainMenu.fxml"));

    }

    private void clearFields(){
        for(Map.Entry i : filterFields.entrySet()){
            TextField textField = (TextField) i.getKey();
            textField.setText("");
        }
    }
}
