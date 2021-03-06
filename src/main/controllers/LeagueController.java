package main.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import main.dao.DBConnection;
import main.model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;

public class LeagueController implements Initializable {


    /**
     * league name
     */
    private String league;


    /**
     * connection of database
     */
    private DBConnection dc;


    public String getLeague() {
        return league;
    }


    public void setLeague(String league) {
        this.league = league;
    }


    /**
     * TextField {{{
     */

    @FXML
    private TextField setGroupName;

    @FXML
    private TextField setRefereeA;

    @FXML
    private TextField setRefereeB;

    @FXML
    private TextField setRefereeAssistantA;

    @FXML
    private TextField setRefereeAssistantB;

    @FXML
    private TextField setRefereeAssistantC;

    @FXML
    private TextField setRefereeAssistantD;

    @FXML
    private TextField setFieldA;

    @FXML
    private TextField setFieldB;

    /**
     * }}}
     */


    /**
     * TableView {{{
     */

    @FXML
    private TableView<Group> configTableView;

    @FXML
    private TableColumn<Group, String> columnName;

    @FXML
    private TableColumn<Group, String> columnFirstPlace;

    @FXML
    private TableColumn<Group, String> columnSecondPlace;

    @FXML
    private TableColumn<Group, String> columnThirdPlace;

    @FXML
    private TableColumn<Group, String> columnRefereeA;

    @FXML
    private TableColumn<Group, String> columnRefereeB;

    @FXML
    private TableColumn<Group, String> columnRefereeAssistantA;

    @FXML
    private TableColumn<Group, String> columnRefereeAssistantB;

    @FXML
    private TableColumn<Group, String> columnRefereeAssistantC;

    @FXML
    private TableColumn<Group, String> columnRefereeAssistantD;

    @FXML
    private TableColumn<Group, String> columnFieldA;

    @FXML
    private TableColumn<Group, String> columnFieldB;

    /**
     * }}}
     */


    /**
     * Button {{{
     */
    @FXML
    private Button addGroupBtn;

    @FXML
    private Button deleteGroupBtn;

    @FXML
    private Button reloadBtn;

    @FXML
    private Button updateGroupInfoBtn;

    /**
     * }}}
     */


    @FXML
    public void addGroupAction() {
        try {

            Connection connection = dc.connection();

            String sql = "insert into " + league + "(Name, RefereeA, RefereeB, RefereeAssistantA, RefereeAssistantB, RefereeAssistantC," +
                    " RefereeAssistantD, FieldA, FieldB)" +
                    " values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, setGroupName.getText());
            pstmt.setString(2, setRefereeA.getText());
            pstmt.setString(3, setRefereeB.getText());
            pstmt.setString(4, setRefereeAssistantA.getText());
            pstmt.setString(5, setRefereeAssistantB.getText());
            pstmt.setString(6, setRefereeAssistantC.getText());
            pstmt.setString(7, setRefereeAssistantD.getText());
            pstmt.setString(8, setFieldA.getText());
            pstmt.setString(9, setFieldB.getText());

            pstmt.executeUpdate();

            update(connection);

            pstmt.close();
            connection.close();


        } catch (SQLException e) {

            e.printStackTrace();

        }


    }


    /**
     * 将Group信息从数据库中删除
     */
    public void deleteGroupAction() {

        try {

            Connection connection = dc.connection();
            /**
             * 获取需要删除的Group的ID
             */
            int ID = configTableView.getSelectionModel().getSelectedItem().getID();

            /**
             * 从数据库中删除记录的sql
             */
            String sql = "DELETE FROM " + league + " WHERE ID = '" + ID + "'";

            /**
             * 执行语句
             */
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();

            /**
             * 更新画板
             */
            update(connection);

            /**
             * 关闭connection
             */
            pstmt.close();
            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }

    @FXML
    public void reloadAction() {

        try {

            Connection connection = dc.connection();

            update(connection);

            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }


    @FXML
    public void configDetail(MouseEvent event) {
        if (event.getClickCount() == 2) {
            try {

                /**
                 * 获取被双击的对象
                 */
                String groupName = configTableView.getSelectionModel().getSelectedItem().getName();
                GroupController controller = new GroupController(groupName);

                Stage stage = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/fxml/group.fxml"));
                loader.setController(controller);
                Parent root = (Parent) loader.load();
                stage.setScene(new Scene(root));

                stage.show();

            } catch (IOException e) {

                e.printStackTrace();

            }


        }

    }


    @FXML
    public void updateGroupInfoAction() {
        try {
            Group group = configTableView.getSelectionModel().getSelectedItem();
            String groupName = group.getName();
            if (groupName != "") {

                Connection connection = dc.connection();

                String getGroupInfoSql = "SELECT * FROM League WHERE Name = '" + groupName + "'";
                ResultSet getGroupInfoRs = connection.createStatement().executeQuery(getGroupInfoSql);
                getGroupInfoRs.next();

                System.out.println(getGroupInfoSql);
                System.out.println(getGroupInfoRs.getString(6));
                System.out.println(getGroupInfoRs.getString(7));
                System.out.println(getGroupInfoRs.getString(8));
                System.out.println(getGroupInfoRs.getString(9));
                System.out.println(getGroupInfoRs.getString(10));
                System.out.println(getGroupInfoRs.getString(11));
                System.out.println(getGroupInfoRs.getString(12));
                System.out.println(getGroupInfoRs.getString(13));


                String updateRefereeA = ((Objects.equals(setRefereeA.getText(), "")) ? getGroupInfoRs.getString(6) : setRefereeA.getText());
                String updateRefereeB = ((Objects.equals(setRefereeB.getText(), "")) ? getGroupInfoRs.getString(7) : setRefereeB.getText());
                String updateRAA = ((Objects.equals(setRefereeAssistantA.getText(), "")) ? getGroupInfoRs.getString(8) : setRefereeAssistantA.getText());
                String updateRAB = ((Objects.equals(setRefereeAssistantB.getText(), "")) ? getGroupInfoRs.getString(9) : setRefereeAssistantB.getText());
                String updateRAC = ((Objects.equals(setRefereeAssistantC.getText(), "")) ? getGroupInfoRs.getString(10) : setRefereeAssistantC.getText());
                String updateRAD = ((Objects.equals(setRefereeAssistantD.getText(), "")) ? getGroupInfoRs.getString(12) : setRefereeAssistantD.getText());
                String updateFieldA = ((Objects.equals(setFieldA.getText(), "")) ? getGroupInfoRs.getString(12) : setFieldA.getText());
                String updateFieldB = ((Objects.equals(setFieldB.getText(), "")) ? getGroupInfoRs.getString(13) : setFieldB.getText());

                System.out.println("updateRefereeA: " + updateRefereeA + " updateRefereeB: " + updateRefereeB);

                String updateSql = "UPDATE League SET RefereeA = '" + updateRefereeA + "', RefereeB = '" + updateRefereeB + "', " +
                        "RefereeAssistantA = '" + updateRAA + "', RefereeAssistantB = '" + updateRAB + "', RefereeAssistantC = '" + updateRAC + "', RefereeAssistantD = '" +
                        updateRAD + "', FieldA = '" + updateFieldA + "', FieldB = '" + updateFieldB + "' WHERE League.Name = '" + groupName + "'";
                PreparedStatement updatePstmt = connection.prepareStatement(updateSql);
                updatePstmt.executeUpdate();
                updatePstmt.close();

                update(connection);
                connection.close();
            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }



    /**
     * 每次删除或者添加球队记录，则重新载入球队记录
     *
     */
    public void update(Connection connection) {


        /**
         * 我也不知道这是什么。。。, but it works!
         */
        columnName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getName());
                return sp;
            }
        });
        columnFirstPlace.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getFirst_place().getName());
                return sp;
            }
        });
        columnSecondPlace.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getSecond_place().getName());
                return sp;
            }
        });
        columnThirdPlace.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getThird_place().getName());
                return sp;
            }
        });
        columnRefereeA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getRefereeAName());
                return sp;
            }
        });
        columnRefereeB.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getRefereeBName());
                return sp;
            }
        });
        columnRefereeAssistantA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getRefereeAssistantAName());
                return sp;

            }
        });
        columnRefereeAssistantB.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getRefereeAssistantBName());
                return sp;
            }
        });
        columnRefereeAssistantC.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getRefereeAssistantCName());
                return sp;
            }
        });
        columnRefereeAssistantD.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getRefereeAssistantDName());
                return sp;
            }
        });
        columnFieldA.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getFieldAName());
                return sp;
            }
        });
        columnFieldB.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> groupStringCellDataFeatures) {
                StringProperty sp = new SimpleStringProperty();
                sp.setValue(groupStringCellDataFeatures.getValue().getFieldBName());
                return sp;
            }
        });

        // System.out.printf("location: " + location.toString()  + " resources: " + resources.toString());

        configTableView.setItems(null);
        configTableView.setItems(loadDataFromDatabase(connection));

        /**
         * 输入之后将TextField设置为空
         */
        setGroupName.clear();
        setRefereeA.clear();
        setRefereeB.clear();
        setRefereeAssistantA.clear();
        setRefereeAssistantB.clear();
        setRefereeAssistantC.clear();
        setRefereeAssistantC.clear();
        setRefereeAssistantD.clear();
        setFieldA.clear();
        setFieldB.clear();

    }


    /**
     * 从数据库中获取数据(传递connection也许会加快速度？)
     *
     */
    @FXML
    private ObservableList<Group> loadDataFromDatabase(Connection conn) {

        String sql = "SELECT * FROM `" + league + "` WHERE `ID` >= 1";

        try {

            Connection connection = conn;
            ObservableList<Group> data = FXCollections.observableArrayList();
            ResultSet rs = connection.createStatement().executeQuery(sql);

            while (rs.next()) {
                /**
                 * 从数据库中获取数据并创建Group实例，并添加到data中
                 */
                RefereeAssistant refereeAssistantA = new RefereeAssistant(rs.getString(8));
                RefereeAssistant refereeAssistantB = new RefereeAssistant(rs.getString(9));
                RefereeAssistant refereeAssistantC = new RefereeAssistant(rs.getString(10));
                RefereeAssistant refereeAssistantD = new RefereeAssistant(rs.getString(11));

                Referee refereeA = new Referee(rs.getString(6), refereeAssistantA, refereeAssistantB);
                Referee refereeB = new Referee(rs.getString(7), refereeAssistantC, refereeAssistantD);

                Field fieldA = new Field(rs.getString(12));
                Field fieldB = new Field(rs.getString(13));

                Team first_place = new Team(rs.getString(3));
                Team second_place = new Team(rs.getString(4));
                Team third_place = new Team(rs.getString(5));

                Group group = new Group(rs.getInt(1), rs.getString(2), first_place, second_place, third_place, refereeA, refereeB, refereeAssistantA, refereeAssistantB, refereeAssistantC, refereeAssistantD, fieldA, fieldB);

                data.add(group);
            }

            return data;


        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }



    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        setLeague("League");

        dc = new DBConnection();

        try {


            Connection connection = dc.connection();
            /**
             * 如果数据库中没有league表，则创建一个
             */
            String sql = "CREATE TABLE IF NOT EXISTS `" + league + "` (" +
                    "`ID` INT UNSIGNED AUTO_INCREMENT," +
                    "`Name` VARCHAR(100) NOT NULL," +
                    "`First_Place` VARCHAR(100) ," +
                    "`Second_Place` VARCHAR(100) ," +
                    "`Third_Place` VARCHAR(100) ," +
                    "`RefereeA` VARCHAR(100) NOT NULL," +
                    "`RefereeB` VARCHAR(100) NOT NULL," +
                    "`RefereeAssistantA` VARCHAR(100) NOT NULL," +
                    "`RefereeAssistantB` VARCHAR(100) NOT NULL," +
                    "`RefereeAssistantC` VARCHAR(100) NOT NULL," +
                    "`RefereeAssistantD` VARCHAR(100) NOT NULL," +
                    "`FieldA` VARCHAR(100) NOT NULL," +
                    "`FieldB` VARCHAR(100) NOT NULL," +
                    "PRIMARY KEY ( `ID` )) ENGINE=InnoDB DEFAULT CHARSET=utf8;";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.executeUpdate();

            /**
             * 载入数据
             */
            update(connection);

            pstmt.close();
            connection.close();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }
}
