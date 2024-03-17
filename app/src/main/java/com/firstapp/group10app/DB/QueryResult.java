package com.firstapp.group10app.DB;

import android.annotation.SuppressLint;
import android.database.Cursor;

import java.sql.ResultSet;
import java.sql.SQLException;

public class QueryResult {
    private Cursor cursor;
    private ResultSet resultSet;

    public QueryResult(Cursor cursor) {
        this.cursor = cursor;
    }

    public QueryResult(ResultSet resultSet) {
        this.resultSet = resultSet;
    }

    public boolean next() throws SQLException {
        if (cursor != null) {
            return cursor.moveToNext();
        } else if (resultSet != null) {
            return resultSet.next();
        } else {
            throw new IllegalStateException("Both Cursor and ResultSet are null");
        }
    }

    @SuppressLint("Range")
    public String getString(String columnName) throws SQLException {
        if (cursor != null) {
            return cursor.getString(cursor.getColumnIndex(columnName));
        } else if (resultSet != null) {
            return resultSet.getString(columnName);
        } else {
            throw new IllegalStateException("Both Cursor and ResultSet are null");
        }
    }
}