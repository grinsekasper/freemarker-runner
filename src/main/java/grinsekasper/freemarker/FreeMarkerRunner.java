package grinsekasper.freemarker;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class FreeMarkerRunner {

    /*
        Check arguments and run or throw up.
    */
    public static void main(String[] args) throws Exception {
        if (args.length == 1) {
            new FreeMarkerRunner().run(args[0]);
        } else {
            Exception up = new IllegalArgumentException(
                "You need to provide ONE argument, the path of the template file."
            );
            throw up;
        }
    }
    
    /*
        Process the template with a minimal but usable configuration
        and almost no data.
    */
    public void run(String path) throws Exception{
    
        Configuration config = new Configuration(Configuration.VERSION_2_3_27);
        config.setDirectoryForTemplateLoading(new File("."));
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        config.setLogTemplateExceptions(false);
        config.setWrapUncheckedExceptions(true);
        
        Template template = config.getTemplate(path.trim());
        
        Map <String, Object> data = 
            hashMap( 
                keyValue("now", LocalDateTime.now())
            );
        
        Writer out = new OutputStreamWriter(System.out);
        template.process(data, out);
        out.flush();
    }
    
    
    /*
        The functions hashMap, keyValue and the class KeyValue
        are syntactic sugar to create hash maps of type 
        java.util.HashMap <String, Object>.       
    */
    private  Map <String, Object> hashMap(KeyValue ...keyValues) {
        Map <String, Object> result = new HashMap <> ();
        for (KeyValue keyValue : keyValues) {
            if(!result.containsKey(keyValue.key)) {
                result.put(keyValue.key, keyValue.value);
            }
        }
        return result;
    }
    
    private  KeyValue keyValue(String key, Object value) {
        return new KeyValue(key, value);
    }
    
    class KeyValue {
        String key;
        Object value;
        KeyValue(String key, Object value) {
            this.key = key;
            this.value = value;
        }
    }
}
