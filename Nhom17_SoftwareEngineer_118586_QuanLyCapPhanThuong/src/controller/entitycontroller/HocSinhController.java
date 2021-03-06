package controller.entitycontroller;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import entity.HocSinh;
import entity.PhanQua;
import service.DatabaseConnection;

public class HocSinhController extends EntityController<HocSinh> {

    @Override
    public void getDataFromDatabase(int limit, int page) {
        ResultSet result = DatabaseConnection.executeQuery("Select * from hoc_sinh order by diachi asc LIMIT " + Integer.toString(limit)
                + " OFFSET " + Integer.toString(page * limit));

        if (list != null) {
            list.clear();
        } else {
            list = new ArrayList<>();
        }

        try {
            metaData = result.getMetaData();
            while (result.next()) {
                list.add(new HocSinh(result.getInt("ID"), result.getString("hoTen"), result.getString("lop"),
                        result.getString("truong"), result.getString("hocLuc"), result.getString("thanhTichDacBiet"),
                        result.getString("diaChi"), result.getString("minhChungHocLuc"),
                        result.getString("minhChungThanhTich")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void getDataFromDatabase() {
        ResultSet result = DatabaseConnection.executeQuery("Select * from hoc_sinh order by diaChi asc");

        if (list != null) {
            list.clear();
        } else {
            list = new ArrayList<>();
        }

        try {
            metaData = result.getMetaData();
            while (result.next()) {
                list.add(new HocSinh(result.getInt("ID"), result.getString("hoTen"), result.getString("lop"),
                        result.getString("truong"), result.getString("hocLuc"), result.getString("thanhTichDacBiet"),
                        result.getString("diaChi"), result.getString("minhChungHocLuc"),
                        result.getString("minhChungThanhTich")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addRecord(HocSinh record) {
        DatabaseConnection.executeUpdate("INSERT INTO hoc_sinh VALUES (null, '" + record.getHoTen() + "', '"
                + record.getLop() + "', '" + record.getTruong() + "', '" + record.getHocLuc() + "', '"
                + record.getThanhTichDacBiet() + "', '" + record.getMinhChungHocLuc() + "', '"
                + record.getMinhChungThanhTich() + "', '" + record.getDiaChi() + "')");
    }

    @Override
    public void updateRecord(HocSinh newRecord) {
        DatabaseConnection.executeUpdate("UPDATE hoc_sinh SET " + "hoTen = '" + newRecord.getHoTen() + "', " + "lop = '"
                + newRecord.getLop() + "', " + "truong = '" + newRecord.getTruong() + "', " + "hocLuc = '"
                + newRecord.getHocLuc() + "', " + "thanhTichDacBiet = '" + newRecord.getThanhTichDacBiet() + "', "
                + "diaChi = '" + newRecord.getDiaChi() + "', " + "minhChungHocLuc = '" + newRecord.getMinhChungHocLuc()
                + "', " + "minhChungThanhTich = '" + newRecord.getMinhChungThanhTich() + "' WHERE ID = "
                + newRecord.getID());
    }

    @Override
    public void updateRecord(HocSinh oldRecord, HocSinh newRecord) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteRecord(int index) {
        // TODO Auto-generated method stub

    }

    @Override
    public void deleteRecord(HocSinh record) {
        DatabaseConnection.executeUpdate("Delete from hoc_sinh where hoc_sinh.ID = " + record.getID() + ";");
    }

    public ArrayList<PhanQua> getAllGift() {
        ArrayList<PhanQua> returnResult = new ArrayList<>();

        ResultSet result = DatabaseConnection.executeQuery("Select * from phan_qua where loaiQua like '%Khuy???n h???c%'");

        try {
            while (result.next()) {
                returnResult.add(new PhanQua(result.getInt("ID"), result.getString("loaiQua"), result.getInt("giaTri"),
                        result.getString("moTa")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return returnResult;
    }

    public Map<String, Integer> getAllStudentsPerType() {
        HashMap<String, Integer> map = new HashMap<>();

        ResultSet result = DatabaseConnection
                .executeQuery("SELECT hoc_sinh.hocLuc, COUNT(*) as Num FROM hoc_sinh GROUP BY hoc_sinh.hocLuc");

        try {
            while (result.next()) {
                map.put(result.getString("hocLuc"), result.getInt("Num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        result = DatabaseConnection.executeQuery(
                "SELECT COUNT(*) as Num FROM hoc_sinh WHERE hoc_sinh.thanhTichDacBiet IS NOT NULL AND hoc_sinh.thanhTichDacBiet != ''");

        try {
            while (result.next()) {
                map.put("Th??nh t??ch ?????c bi???t", result.getInt("Num"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    // index{"Gi???i", "Ti??n ti???n", "Trung b??nh", "Th??nh t??ch ?????c bi???t"};
    public void giveGift(Date theGivingDay, int studyYear, ArrayList<PhanQua> phanQuas, Integer[] index, int sumGift,
            int sumValue) throws SQLException {
        ResultSet result1 = DatabaseConnection.executeQuery(
                "SELECT * FROM phan_qua_khuyen_hoc WHERE phan_qua_khuyen_hoc.date = '" + theGivingDay + "'");

        if (result1.next()) {
            throw new SQLException();
        }
        DatabaseConnection.executeUpdate("INSERT INTO phan_qua_khuyen_hoc VALUES ('" + theGivingDay + "', "
                + phanQuas.get(index[0]).getID() + ", " + phanQuas.get(index[1]).getID() + ", "
                + phanQuas.get(index[2]).getID() + ", " + sumGift + ", " + sumValue + ")");
        ResultSet result = DatabaseConnection.executeQuery("Select * from hoc_sinh where 1");

        ArrayList<HocSinh> arrayList = new ArrayList<>();

        try {
            while (result.next()) {
                arrayList.add(new HocSinh(result.getInt("ID"), null, null, null, result.getString("hocLuc"),
                        result.getString("thanhTichDacBiet"), null, null, null));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < arrayList.size(); i++) {
            int gID = -1;
            if (arrayList.get(i).getHocLuc().equals("Gi???i")) {
                gID = phanQuas.get(index[0]).getID();
            }
            if (arrayList.get(i).getHocLuc().equals("Ti??n ti???n")) {
                gID = phanQuas.get(index[1]).getID();
            }
            if (arrayList.get(i).getHocLuc().equals("Trung b??nh")) {
                gID = phanQuas.get(index[2]).getID();
            }

            if (arrayList.get(i).getThanhTichDacBiet() != null && !arrayList.get(i).getThanhTichDacBiet().isBlank()) {
                DatabaseConnection.executeUpdate("INSERT INTO `phat_qua_cuoi_nam` VALUES (null, "
                        + arrayList.get(i).getID() + ", '" + theGivingDay + "', " + studyYear + ", "
                        + phanQuas.get(index[0]).getID()  + ")");
            }

            DatabaseConnection.executeUpdate("INSERT INTO `phat_qua_cuoi_nam` VALUES (null, " + arrayList.get(i).getID()
                    + ", '" + theGivingDay + "', " + studyYear + ", " + gID  + ")");
        }

    }

    public static boolean searchDiaChi(String s){
        ResultSet resultSet = DatabaseConnection.executeQuery("Select * from ho_gia_dinh where diaChi = '"+
        s + "'" );
        try {
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;

    }  
    
}
