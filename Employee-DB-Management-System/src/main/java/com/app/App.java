package com.app;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import org.hibernate.query.Query;



import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;



public class App 
{
	public static Scanner scan = new Scanner(System.in);
    public static void main( String[] args )
    {
    	
		System.out.println("Welcome to Employee Data Manager!");
		
		boolean cont = true;
		while(cont) {
			System.out.println("Please tell what task you would like to perform =>\n 1- Add\n 2- Update\n 3- Get all Employee details\n 4- Like\n 5- Logical operator\n 6- Exit\n");
			int opt = scan.nextInt();
			switch(opt) {
			case 1:
				addEmployee();
				break;
			case 2:
				System.out.println("Enter id of employee to update: ");
				int eid = scan.nextInt();
				scan.nextLine();
				updateEmployee(eid);
				break;
			case 3:
				usingNamedQuery();
				break;
			case 4:
				usingLike();
				break;
			case 5:
				usingLogical();
				break;
			case 6:
				System.out.println("Exiting...\n Thanks for using the EMS");
				cont=false;
				break;
			default:
				System.out.println("Please enter a valid option");
			}
		}	
    }
    
    

	private static void addEmployee() {
		Configuration c = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sf = c.buildSessionFactory();
		Session s = sf.openSession();
		try {
			
			Transaction t = s.beginTransaction();
			Employee emp = new Employee();
			//System.out.println("Enter employee ID: ");
			//emp.setId(scan.nextInt());
			//scan.nextLine();
			
			System.out.println("Enter employee First Name: ");
			scan.nextLine();
			emp.setFirstName(scan.nextLine());
			
			System.out.println("Enter employee Last name: ");
			emp.setLastName(scan.nextLine());
			
			System.out.println("Enter employee Designation: ");
			emp.setDesignation(scan.nextLine());
			
			s.save(emp);
			t.commit();
			
			System.out.println("Saved successfully");
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			s.close();
			
		}
		
	}
    
    private static void updateEmployee(int id) {
    	Configuration c = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sf = c.buildSessionFactory();
		Session s = sf.openSession();
		try {
			Transaction t = s.beginTransaction();
			Query<Employee> q= s.createQuery("from Employee where id = :id");
			q.setParameter("id", id);
			Employee emp = q.getSingleResult();
			System.out.println("Enter updated details for this selection?\n "+ emp.toString());
			System.out.println("Enter id: ");
			emp.setId(scan.nextInt());
			scan.nextLine();
			System.out.println("Enter first Name: ");
			emp.setFirstName(scan.nextLine());
			System.out.println("Enter Last Name: ");
			emp.setLastName(scan.nextLine());
			System.out.println("Enter designation: ");
			emp.setDesignation(scan.nextLine());
			
			s.save(emp);
			t.commit();
		}
		catch(Exception e){
			System.out.println(e);
		}
		finally {
			s.close();
			System.out.println("Updated successfully");			
		}		
    }
    
    private static void usingNamedQuery() {
    	Configuration c = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sf = c.buildSessionFactory();
		Session s = sf.openSession();
		try {
			Transaction t = s.beginTransaction();
			Query<Employee> query = s.getNamedQuery("getAllEmployeeDetails");
			List<Employee> list = query.getResultList();
			Iterator<Employee> i = list.iterator();
			while(i.hasNext()) {
				System.out.println(i.next());
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			s.close();
		}
    }
    
    private static void usingLike() {
    	Configuration c = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sf = c.buildSessionFactory();
		Session s = sf.openSession();
		try {
			Transaction t = s.beginTransaction();
			CriteriaBuilder cb = s.getCriteriaBuilder();
			CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
			
			Root<Employee> root = cq.from(Employee.class);
			
			cq.select(root).where(cb.like(root.get("lastName").as(String.class), "%k%"));
			
			Query<Employee> query = s.createQuery(cq);
			List<Employee> list = query.getResultList();
			
			for(Employee emp : list) {
				System.out.println(emp);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			s.close();
		}
    }
    
    private static void usingLogical() {
    	Configuration c = new Configuration().configure("hibernate.cfg.xml");
		SessionFactory sf = c.buildSessionFactory();
		Session s = sf.openSession();
		try {
			Transaction t = s.beginTransaction();
			CriteriaBuilder cb = s.getCriteriaBuilder();
			CriteriaQuery<Employee> cq = cb.createQuery(Employee.class);
			
			Root<Employee> root = cq.from(Employee.class);
			
			cq.select(root).where(
					cb.and(
							cb.like(root.get("firstName").as(String.class), "%s%"),
							cb.equal(root.get("designation").as(String.class), "Front End Developer")));
			
			Query<Employee> query = s.createQuery(cq);
			List<Employee> list = query.getResultList();
			
			for(Employee emp : list) {
				System.out.println(emp);
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		finally {
			s.close();
		}
    }
    
    
    
}
