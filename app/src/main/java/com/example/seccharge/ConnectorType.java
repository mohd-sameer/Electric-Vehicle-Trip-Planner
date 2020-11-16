package com.example.seccharge;

public class ConnectorType {

    private String connector_type;
    private String id;

    public ConnectorType(String connector_id, String connector_type) {
        this.connector_type = connector_type;
        this.id = connector_id;
    }

    public String getConnector_type() {
        return connector_type;
    }

    public void setConnector_type(String connector_type) {
        this.connector_type = connector_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
