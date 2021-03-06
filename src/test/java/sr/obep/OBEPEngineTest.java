package sr.obep;

import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.jena.riot.system.IRIResolver;
import org.parboiled.Parboiled;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.ReportingParseRunner;
import org.parboiled.support.ParsingResult;
import sr.obep.parser.delp.DELPParser;
import sr.obep.parser.delp.EventCalculusDecl;
import sr.obep.parser.delp.EventDecl;
import sr.obep.querying.OBEPQuery;
import sr.obep.querying.OBEPQueryImpl;

import java.io.File;
import java.io.IOException;


public class OBEPEngineTest extends TestCase {

    public void setUp() {
        String input = getFile("src/test/resources/test.query");
        System.out.println(input);
        DELPParser parser = Parboiled.createParser(DELPParser.class);

        parser.setResolver(IRIResolver.create());

        ParsingResult<OBEPQuery> result = new ReportingParseRunner(parser.Query()).run(input);

        if (result.hasErrors()) {
            System.out.println("Errors have been found!");
            for (ParseError arg : result.parseErrors) {
                System.out.println(input.substring(0, arg.getStartIndex()) + "|->" + input.substring(arg.getStartIndex(), arg.getEndIndex()) + "<-|" + input.substring(arg.getEndIndex(), input.length() - 1));
            }
        }

        OBEPQueryImpl q = (OBEPQueryImpl) result.resultValue;
        for (EventDecl event : q.getEventCalculusDecls()) {
            EventCalculusDecl ecd = (EventCalculusDecl) event;
            System.out.println(ecd);
        }
    }

    public void testQuery() {

    }

    private String getFile(String fileName) {

        String result = "";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            result = IOUtils.toString(new File(fileName).toURI());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;

    }
}
