import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class NdebPullerMain {

    public final ObjectMapper mapper = new ObjectMapper();

    public NdebPullerMain(){
        mapper.registerModule(new Jdk8Module());
    }

    private void sendGet() throws Exception {
        List<Question> questions = new ArrayList<>();
        String url = "http://www.quizzn.com/quiz/58/";
        Document doc = Jsoup.connect(url).get();
        Elements byClass = doc.getElementsByClass("post format-standard ");
        for (Element el : byClass){
            Elements allOptions = el.select("p");
            List<String> options = new ArrayList<>();
            for(Element e: allOptions){
                options.add(e.text());
            }
            Element rightOption = el.getElementsByClass("text-success").first();

            Question question = new Question(UUID.randomUUID().toString(),
                    el.select("a").first().text(),
                    options,
                    rightOption.text(),
                    0,
                    0,
                    false,
                    "testBatch");
            questions.add(question);
        }
        writeToFile(questions);
    }

    private void writeToFile(List<Question> questions){
        try {
            BufferedWriter bw = Files.newBufferedWriter(Paths.get("/home/arun/questions.txt"));
            for(Question q : questions){
                bw.write(mapper.writeValueAsString(q) + "\n");
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]){
        try {
            NdebPullerMain puller = new NdebPullerMain();

            puller.sendGet();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
