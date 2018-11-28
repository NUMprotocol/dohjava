/*
MIT License

Copyright (c) 2018 NUM Technology Ltd

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the Software without restriction, including without limitation
the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of
the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package uk.num.dohjava;

import org.junit.Test;
import org.xbill.DNS.*;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by alex on 27/11/2018.
 */
public class BasicTest {

    @Test
    public void testBasicResolve() throws IOException {
        // Send a DNS request use dohjava and check the response

//        String host = "https://dns.google.com/resolve"; // TODO Looks like Google only do JSON?
        String host = "https://cloudflare-dns.com/dns-query";
        String query = "bbc.co.uk.";
        Integer timeout = 5000; // milliseconds

        DohResolver dohResolver = new DohResolver(host);

        Record queryTxtRecord = Record.newRecord(new Name(query), Type.A, DClass.IN);
        Message queryMessage = Message.newQuery(queryTxtRecord);

        Message response = dohResolver.query(queryMessage, timeout);

        System.out.println(response.toString());

        assertEquals(Rcode.NOERROR, response.getRcode());

        assertTrue(response.getSectionRRsets(Section.ANSWER).length > 0);
    }
}
