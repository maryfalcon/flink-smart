package org.myorg.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import com.datastax.driver.core.utils.Bytes;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CassandraPersistor {

    private Cluster cluster;
    private Session session;

    private final static String CONTACT_POINT = "127.0.0.1";
    private final static String KEYSPACE = "flinkdb";

    public CassandraPersistor() {
        cluster = Cluster.builder().addContactPoint("127.0.0.1").build();
        session = cluster.connect("flinkdb");
    }

    public Data insertData(Data data) {
        data.setId(UUID.randomUUID().toString());
        session.execute("INSERT INTO data(id,file,name, extension, owner_id, date, place) \n"
                + "VALUES (" + data.getId() + ","+Bytes.toHexString(data.getFile())+" ,'" + data.getName() + "', '" + data.getExtension() + "'," + data.getOwnerId() + "," + data.getDate().getTime() + ", '" + data.getPlace() + "');");
        return data;
    }

    public Data updateData(Data data) {
        session.execute("UPDATE data SET file = " + Bytes.toHexString(data.getFile()) + ",name = '" + data.getName() + "', extension = '" + data.getExtension() + "',owner_id = " + data.getOwnerId() + ",date = " + data.getDate().getTime() + ", place = '" + data.getPlace() + "' WHERE id = " + data.getId() + ";");
        return data;
    }

    public Data getDataByName(String name) {
        Statement statement = QueryBuilder.select().from("flinkdb", "data") .where(eq("name", name));
        return getDataFromRow(session.execute(statement).one());
    }

    public Data getDataById(String id) {
        Statement statement = QueryBuilder.select().from("flinkdb", "data") .where(eq("id", UUID.fromString(id)));
        return getDataFromRow(session.execute(statement).one());
    }

    public List<Data> getAllData() {
        List<Data> dataList = new ArrayList<>();
        Statement statement = QueryBuilder.select().all().from("flinkdb", "data");
        for (Row row : session.execute(statement).all()) {
            dataList.add(getDataFromRow(row));
        }
        return dataList;
    }
    
    private Data getDataFromRow(Row row){
        Data data = new Data();
        data.setId(row.getUUID("id").toString());
        data.setFile(row.getBytes("file").array());
        data.setName(row.getString("name"));
        data.setDate(row.getTimestamp("date"));
        data.setPlace(row.getString("place"));
        data.setExtension(row.getString("extension"));
        data.setOwnerId(row.getInt("owner_id"));
        return data;
    }
    
   public void close(){
       session.close();
       cluster.close();
   }

}
