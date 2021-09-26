package jp.gr.java_conf.tsyki.javafx;

import java.net.URL;
import java.util.Comparator;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javafx.util.converter.DefaultStringConverter;

public class SampleController implements Initializable{

	public static class ReadOnlyTableModel{
		private int no;
		private String text;

		public ReadOnlyTableModel(int no, String text) {
			this.no = no;
			this.text = text;
		}

		public int getNo() {
			return no;
		}

		public void setNo(int no) {
			this.no = no;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		@Override
		public int hashCode() {
			return Objects.hash(no, text);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ReadOnlyTableModel other = (ReadOnlyTableModel) obj;
			return no == other.no && Objects.equals(text, other.text);
		}
	}

	public static class EditableTableModel{
		private SimpleIntegerProperty no;
		private SimpleStringProperty text;

		public EditableTableModel(int no, String text) {
			this.no = new SimpleIntegerProperty(no);
			this.text = new SimpleStringProperty(text);
		}

		public IntegerProperty noProperty(){
			return no;
		}
		public StringProperty textProperty(){
			return text;
		}

		public void setText(String text) {
			this.text.setValue(text);
		}

	}

	@FXML
	private TextField addText;

	@FXML
	private Button addButton;

	@FXML
	private TableView<ReadOnlyTableModel> readOnlyTable;

	@FXML
	private TableView<EditableTableModel> editableTable;

	private ObservableList<EditableTableModel> models;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// 編集不可のテーブルの列追加
		{
			TableColumn<ReadOnlyTableModel, String> noCol = new TableColumn<>("No");
			noCol.setMinWidth(30);
			noCol.setMaxWidth(30);
			noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
			readOnlyTable.getColumns().add(noCol);

			TableColumn<ReadOnlyTableModel, String> textCol = new TableColumn<>("テキスト");
			textCol.setMinWidth(300);
			textCol.setCellValueFactory(new PropertyValueFactory<>("text"));
			readOnlyTable.getColumns().add(textCol);
		}
		// 編集可なテーブルの列追加
		{
			models = FXCollections.observableArrayList();
			editableTable.setItems(models); 

			editableTable.setEditable(true);
			// No列は編集不可
			TableColumn<EditableTableModel, String> noCol = new TableColumn<>("No");
			noCol.setMinWidth(30);
			noCol.setMaxWidth(30);
			noCol.setCellValueFactory(new PropertyValueFactory<>("no"));
			editableTable.getColumns().add(noCol);
			// text列は編集可に
			TableColumn<EditableTableModel, String> textCol = new TableColumn<>("テキスト");
			textCol.setMinWidth(300);
			textCol.setCellValueFactory(new PropertyValueFactory<>("text"));
			textCol.setCellFactory(new Callback<TableColumn<EditableTableModel,String>, TableCell<EditableTableModel,String>>() {
				@Override
				public TableCell<EditableTableModel, String> call(TableColumn<EditableTableModel, String> arg0) {
					return new TextFieldTableCell<EditableTableModel, String>(new DefaultStringConverter());
				}
			});
			textCol.setOnEditCommit(new EventHandler<CellEditEvent<EditableTableModel, String>>() {           
				@Override
				public void handle(CellEditEvent<EditableTableModel, String> t) {
					((EditableTableModel) t.getTableView().getItems().get(t.getTablePosition().getRow())).setText(t.getNewValue());
				}
			});	
			editableTable.getColumns().add(textCol);
		}
	}

	@FXML
	public void add(ActionEvent event) {
		Integer maxNo = readOnlyTable.getItems().stream().map(ReadOnlyTableModel::getNo).max(Comparator.naturalOrder()).orElse(0);
		readOnlyTable.getItems().add(new ReadOnlyTableModel(maxNo + 1, addText.getText()));
		editableTable.getItems().add(new EditableTableModel(maxNo + 1, addText.getText()));
	}
}
