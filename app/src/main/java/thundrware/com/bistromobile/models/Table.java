package thundrware.com.bistromobile.models;

public class Table {
    private int AreaId;
    private int TableNumber;

    public int getDisplayAreaId() { return AreaId + 1; }

    public int getDisplayTableNumber() { return TableNumber + 1; }

    public int getAreaId() {
        return AreaId;
    }

    public void setAreaId(int areaId) {
        this.AreaId = areaId;
    }

    public int getTableNumber() {
        return TableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.TableNumber = tableNumber;
    }
}
