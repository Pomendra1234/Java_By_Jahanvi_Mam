package com.hibernateproject;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.hibernateproject.util.HibernateUtil;


// import ;
public class InsertStudent {
    public static void main(String[] args) {
        Session session = null;
        Transaction tx=null;

        try {
            session=HibernateUtil.getSessionFactory().openSession();  //to connect with database 
            tx=session.beginTransaction();    //group all operation in one unit
            Student s=new Student(101,"Rahul",25000.0);
            session.persist(s);  //mapping
            tx.commit();
            System.out.println("Student inserted Successfully");
        } catch (Exception e) {
            if(tx!=null){
                tx.rollback();
            }
        }finally{
            if(session!=null){
                session.close();
            }
            HibernateUtil.shutdown();
        }
    }
}
