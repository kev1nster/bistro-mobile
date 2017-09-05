package thundrware.com.bistromobile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import thundrware.com.bistromobile.models.ActiveTable;
import thundrware.com.bistromobile.models.Table;

public class ActiveTablesConverter {

    private String jsonText;
    private WaiterManager waiterManager;

    protected ActiveTablesConverter(String json) {
        jsonText = json;
        waiterManager = new WaiterManager();
    }

    public List<ActiveTable> convert() throws JSONException {
        List<ActiveTable> activeTables = new ArrayList<>();
        JSONArray tablesJsonArray = new JSONArray(jsonText);

        for (int i=0; i < tablesJsonArray.length(); i++) {

            JSONObject jsonObject = tablesJsonArray.getJSONObject(i);
            ActiveTable activeTable = new ActiveTable();

            activeTable.setWaiter(waiterManager.getCurrentWaiter());

            // Table
            int tableId = jsonObject.getInt("Number");
            int areaId = jsonObject.getInt("AreaId");

            Table table = new Table();
            table.setTableNumber(tableId);
            table.setAreaId(areaId);
            activeTable.setTable(table);

            activeTables.add(activeTable);
        }

        return activeTables;
    }

    public static ActiveTablesConverter from(String json) {
        return new ActiveTablesConverter(json);
    }
}
