package com.qx.interactive.answer.utils;

import com.qx.interactive.answer.model.SeatPerson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HeYingXin on 2017/2/21.
 */
public class SaveSeatPersonUtils {

    private static class HolderClass {
        private final static SaveSeatPersonUtils instance = new SaveSeatPersonUtils();
    }
    public static SaveSeatPersonUtils getInstance() {
        return HolderClass.instance;
    }

    public void save(ArrayList<SeatPerson> persons,String fileName) throws Exception {
        saveLocal(persons,fileName);
    }

    public String outPut(ArrayList<SeatPerson> persons,String fileName) throws Exception {
        return saveLocal(persons,fileName);
    }

    private String saveLocal(ArrayList<SeatPerson> persons,String fileName) throws Exception{
        JSONArray jsonArray = new JSONArray();
        for (SeatPerson person:persons){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("seatId",person.seatId);
            jsonObject.put("cardId",person.cardId);
            jsonObject.put("chooseResult",person.chooseResult);
            jsonObject.put("emptySeat",person.emptySeat);
            jsonObject.put("toBeBinding",person.toBeBinding);
            jsonObject.put("mColumu",person.mColumu);
            jsonObject.put("mRow",person.mRow);
            jsonArray.put(jsonObject);
        }
        return executeIo(jsonArray.toString(),fileName);
    }

    private String executeIo(String result,String fileName) throws Exception{
        return SeatFileUtils.writeSDcard(result,fileName);
    }

    public List<SeatPerson> inPut(String fileName) throws Exception {
        String seatSeatting = SeatFileUtils.readSDcard(fileName);
        List<SeatPerson> seatPersons = new ArrayList<>();
        JSONArray array = new JSONArray(seatSeatting);
        if(array!=null){
            for(int i=0;i<array.length();i++){
                JSONObject object = array.getJSONObject(i);
                SeatPerson person = new SeatPerson(object.isNull("seatId")?"":object.getString("seatId"),
                        object.isNull("cardId")?"":object.getString("cardId"),
                        object.isNull("chooseResult")?"":object.getString("chooseResult"),
                        object.isNull("emptySeat")?false:object.getBoolean("emptySeat"),
                        object.isNull("mColumu")?-1:object.getInt("mColumu"),
                        object.isNull("mRow")?-1:object.getInt("mRow"),
                        object.isNull("toBeBinding")?false:object.getBoolean("toBeBinding"));
                seatPersons.add(person);
            }
        }
        return seatPersons;
    }
}
