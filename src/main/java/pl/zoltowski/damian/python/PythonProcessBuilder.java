package pl.zoltowski.damian.python;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PythonProcessBuilder {

    private final String PYTHON = "C:\\Users\\Legion\\AppData\\Local\\Microsoft\\WindowsApps\\python3.9";

    private final String PATH_TO_JSON_GRAPH = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\zoltowski\\damian\\python\\data\\graph\\";
    private final String PATH_TO_RESULT_GRAPH = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\zoltowski\\damian\\python\\result\\graph\\";

    private final String GRAPH_SCRIPT = System.getProperty("user.dir") + "\\src\\main\\java\\pl\\zoltowski\\damian\\python\\scripts\\drawGraph.py";

    public void generateGraphImageToFile(String pcbJsonBoardFileName, String outputBoardFileName) {
        try {
            String pcbJsonFile = PATH_TO_JSON_GRAPH + pcbJsonBoardFileName;
            String resultFile = PATH_TO_RESULT_GRAPH + outputBoardFileName;
            Process process = Runtime.getRuntime().exec(
              new String[] {
                PYTHON,
                GRAPH_SCRIPT,
                pcbJsonFile,
                resultFile
              }
            );
            //for debugging
            String cmdO = null;
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader brEr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while((cmdO = br.readLine()) != null) {
                System.out.println(cmdO);
            }

            while((cmdO = brEr.readLine()) != null) {
                System.out.println(cmdO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
