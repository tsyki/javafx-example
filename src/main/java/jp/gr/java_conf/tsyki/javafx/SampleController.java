package jp.gr.java_conf.tsyki.javafx;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class SampleController {
	@FXML
	private Button searchButton;

	@FXML
	private TextArea searchResultTextArea;

	
	@FXML
	public void search(ActionEvent event) {
		Task<Void> searchTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				String result = "hoge\npiyo\nfuga";
				// 時間がかかる処理の想定でsleep
				Thread.sleep(3000);
				// 部品の更新はアプリケーションスレッドで行わせる
				Platform.runLater(
						() -> {
							searchButton.setDisable(false);
							searchButton.setText("検索");
							searchResultTextArea.setDisable(false);
							searchResultTextArea.setText(result);
						});
				return null;
			}
		};
		// 検索中は再実行されないよう無効化しておく
		searchButton.setDisable(true);
		searchButton.setText("検索中");
		searchResultTextArea.setDisable(true);
		// 非同期処理を実行
		Executor executor = Executors.newSingleThreadExecutor();
		executor.execute(searchTask);
	}
}
