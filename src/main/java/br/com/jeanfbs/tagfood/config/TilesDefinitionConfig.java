package br.com.jeanfbs.tagfood.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.tiles.Attribute;
import org.apache.tiles.Definition;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.request.Request;

public class TilesDefinitionConfig implements DefinitionsFactory {

	private static final Map<String, Definition> TILES_DEFINITIONS = new HashMap<>();
	
		@Override
		public Definition getDefinition(String name, Request rqst) {
	
			return TILES_DEFINITIONS.get(name);
		}
	
		public static void addDefinitions() {
			
			addDefaultLayoutDefinition("home", "Home Page", "");
	
		}
	
		private static void addDefaultLayoutDefinition(String name, String title, String body) {
	
			Map<String, Attribute> attributes = new HashMap<>();
	
			attributes.put("title", new Attribute(title));
			attributes.put("header", new Attribute("/WEB-INF/tiles/templates/header.jsp"));
			attributes.put("body", new Attribute(body));
			attributes.put("footer", new Attribute("/WEB-INF/tiles/templates/footer.jsp"));
	
			Attribute baseTemplate = new Attribute("/WEB-INF/tiles/layouts/default.jsp");
	
			TILES_DEFINITIONS.put(name, new Definition(name, baseTemplate, attributes));
		}
}
