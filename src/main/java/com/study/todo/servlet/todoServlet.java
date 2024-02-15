package com.study.todo.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.study.todo.config.DBConnectionMgr;
import com.study.todo.entity.TodoEntity;


@WebServlet("/todo/post")
public class todoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
  
    public todoServlet() {
        super();
    }

    
   	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 1) Json 파일 받기 (문자열 형태로) StringBuilder , BufferedReader / getReader(), append()
		String requestJsonData = null;
		String readData = null;
		
		StringBuilder builder = new StringBuilder();
		BufferedReader reader = request.getReader();
				
		while((readData = reader.readLine()) != null ) {
			builder.append(readData);
		}
		requestJsonData = builder.toString();
		
		// 2) Gson -> Json -> java 에서 조작 가능한 형태로 (todoEntity 형태로) gson.fromJson()
		Gson gson = new Gson();
		TodoEntity todoEntity = gson.fromJson(requestJsonData, TodoEntity.class);
		
		// 3) DB에 넣기 Connection, PreparedStatement, ResultSet
		
		DBConnectionMgr pool = DBConnectionMgr.getInstance();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			con = pool.getConnection();
			String sql = "insert into todo_tb values (0, ?)";
			pstmt = con.prepareStatement(sql); //쿼리문 작성
			pstmt.setString(1, todoEntity.getTodoContent());
			pstmt.executeUpdate();
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			pool.freeConnection(con, pstmt);
		}
		
		
	}
		
}
