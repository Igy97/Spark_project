package main;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;

public class Glavna {

	public static void main(String[] args) 
	{
		port(5000);
		staticFileLocation("public");
		
		
		
		get("/", (req, res)->{	
			ArrayList<Person> osobe = Data.readFromJson("person.json");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("osobe", osobe);
			return new ModelAndView(model, "index.hbs");		
		}, new HandlebarsTemplateEngine());
		
		get("/editovanje", (req, res)->{	
			ArrayList<Person> osobe = Data.readFromJson("person.json");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("osobe", osobe);
			return new ModelAndView(model, "editovanje.hbs");		
		}, new HandlebarsTemplateEngine());
		
		post("/editovanje", (req, res)->{	
			ArrayList<Person> osobe = Data.readFromJson("person.json");
			Map<String, Object> model = new HashMap<String, Object>();
			int id;
			if(req.queryParams("id") != null)
			{
				id=Integer.parseInt(req.queryParams("id"));
				model.put("osoba", osobe.get(id-1));
				osobe.remove(id-1);
			}
			model.put("osobe", osobe);
			return new ModelAndView(model, "editovanje.hbs");		
		}, new HandlebarsTemplateEngine());
		
		get("/dodavanje", (req, res)->{	
			return new ModelAndView(null, "dodavanje.hbs");		
		}, new HandlebarsTemplateEngine());
		
		
		//filteri index
		post("/api/filtriranje1",(req,res)->{
			res.type("text/text");
			String unos = req.queryParams("unos");
			ArrayList<Person> osobe=new ArrayList<>();
			for(Person p:Data.readFromJson("person.json"))
				if((p.getFirstName().contains(unos) == true) || (p.getLastName().contains(unos) == true)) osobe.add(p);		
			Gson gson = new Gson();
			return gson.toJson(osobe);
		});
	
		
		post("/api/filtriranje2_filtriranje3",(req,res)->{
			res.type("text/text");
			String tip = req.queryParams("tip");
			int elemenat1=Integer.parseInt(req.queryParams("elemenat1"));
			int elemenat2=Integer.parseInt(req.queryParams("elemenat2"));
			ArrayList<Person> osobe=new ArrayList<>();
			if(tip.equals("filtriranje2"))
			{
				for(Person p:Data.readFromJson("person.json"))
					if(p.getLanguage().size() >= elemenat1 && p.getLanguage().size() <= elemenat2) osobe.add(p);
			}
			else
			{
				for(Person p:Data.readFromJson("person.json"))
					if(p.getId() >= elemenat1 && p.getId() <= elemenat2) osobe.add(p);
			}
					
			Gson gson = new Gson();
			return gson.toJson(osobe);
		});
		
		post("/api/filtriranje4",(req,res)->{
			res.type("text/text");
			String unos = req.queryParams("izlaz");
			ArrayList<Person> osobe=new ArrayList<>();
			for(Person p:Data.readFromJson("person.json"))
				if(p.getGender().equals(unos)) osobe.add(p);		
			Gson gson = new Gson();
			return gson.toJson(osobe);
		});
		
		post("/api/filtriranje5",(req,res)->{
			res.type("text/text");
			String unos = req.queryParams("izlaz");
			ArrayList<Person> osobe=Data.readFromJson("person.json");
			
			Person pomocna = osobe.get(0);
			
			if(unos.equals("asc"))
			{
				for(int i = 0; i < osobe.size()-1; i++)
					for(int j = i + 1; j < osobe.size(); j++)
					{
						if(osobe.get(i).getFirstName().compareTo(osobe.get(j).getFirstName()) > 0)
						{
							pomocna = osobe.get(i);
							osobe.set(i, osobe.get(j));
							osobe.set(j, pomocna);
						}
					}	
			}
			else
			{
				for(int i = 0; i < osobe.size()-1; i++)
					for(int j = i + 1; j < osobe.size(); j++)
					{
						if(osobe.get(i).getFirstName().compareTo(osobe.get(j).getFirstName()) < 0)
						{
							pomocna = osobe.get(i);
							osobe.set(i, osobe.get(j));
							osobe.set(j, pomocna);
						}
					}
			}
			
			

			Gson gson = new Gson();
			return gson.toJson(osobe);
		});
		
		
	
	
		
		//editovanje
		post("/api/prikaz_jedne_osobe",(req,res)->{
			res.type("text/text");
			int id = Integer.parseInt(req.queryParams("id"));
			for(Person p:Data.readFromJson("person.json"))
				if(p.getId() == id)
				{
					Gson gson = new Gson();
					return gson.toJson(p);
				}	
			return null;
		});
		
		
		post("/editovanje_jedne_osobe",(req,res)->{
			res.type("text/text");
			int id = Integer.parseInt(req.queryParams("identifikator"));
			boolean izmeni_osobu = Boolean.parseBoolean(req.queryParams("izmeni_osobu"));	
			ArrayList<Person> osobe = Data.readFromJson("person.json");	
			String[] jezici = req.queryParams("jezici").split(",\n");
			ArrayList<Language> lista_jezika = new ArrayList<>();
			for(String s : jezici)
				if(!s.equals("")) lista_jezika.add(new Language(s));
			Person osoba = new Person(id, req.queryParams("ime"),req.queryParams("prezime"), req.queryParams("email"), req.queryParams("pol"), lista_jezika);
			boolean izmena_uradjena = false;
			Person p = null;
			if((id-1) >= osobe.size()) return "id\n";	
			else p = osobe.get(id-1);			
			if(!p.getFirstName().equals(osoba.getFirstName()))
			{
				izmena_uradjena  = true;
				if(izmeni_osobu) p.setFirstName(osoba.getFirstName());		
			}
			if(!p.getLastName().equals(osoba.getLastName()))
			{
				izmena_uradjena  = true;
				if(izmeni_osobu)  p.setLastName(osoba.getLastName());
			}
			if(!p.getEmail().equals(osoba.getEmail()))
			{
				izmena_uradjena  = true;
				if(izmeni_osobu) p.setEmail(osoba.getEmail());
			}
			if(!p.getGender().equals(osoba.getGender()))
			{
				izmena_uradjena  = true;
				if(izmeni_osobu) p.setGender(osoba.getGender());
			}
			if(p.getLanguage().size()!=osoba.getLanguage().size())
			{
				izmena_uradjena  = true;
				if(izmeni_osobu) p.setLanguage(osoba.getLanguage());		
			}
			else
			{
				for(int i = 0; i < p.getLanguage().size() ; i++)	
					if(!p.getLanguage().get(i).getName().equals(osoba.getLanguage().get(i).getName()))
					{
						izmena_uradjena = true;
						if(izmeni_osobu) p.setLanguage(osoba.getLanguage());
						break;								
					}			
			}
				
			if(izmena_uradjena && izmeni_osobu)
			{
				if(Data.writeToJSON(osobe, "person.json")) 
					return "jeste";	
			}
			else if(izmeni_osobu) return "nije";
				
			//brisanje				 
			if(izmena_uradjena == false)
			{
				osobe.remove(osoba.getId()-1);
				for(int i = 0; i < osobe.size(); i++)
					osobe.get(i).setId(i+1);
				if(Data.writeToJSON(osobe, "person.json"))
				{
					return "jeste";			
				}
				else return "nije";
			}
			else return "nije";		
		});
		
		//dodavanje
		post("/dodati_novu_osobu",(req,res)->{
			res.type("text/text");
			String[] jezici = req.queryParams("jezici").split(",\n");
			
			ArrayList<Language> lista_jezika = new ArrayList<>();
			for(String s : jezici)
				if(!s.equals("")) lista_jezika.add(new Language(s));		
				
			Person p = new Person(Integer.parseInt(req.queryParams("identifikator")), req.queryParams("ime"),req.queryParams("prezime"), req.queryParams("email"), req.queryParams("pol"), lista_jezika);
			ArrayList<Person> osobe = Data.readFromJson("person.json");
			osobe.add(p);
			if(Data.writeToJSON(osobe, "person.json")) return "Jeste";
			else return "Nije";
		
		});
		
		post("/ukupan_size_osoba",(req,res)->{
			res.type("text/text");
			return Data.readFromJson("person.json").size() + 1;
		
		});
			
	}

}
