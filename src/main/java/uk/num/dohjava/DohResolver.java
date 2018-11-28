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

import org.xbill.DNS.Message;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;

/**
 * Created by alex on 27/11/2018.
 */
public class DohResolver {
    String host;
    public DohResolver(String host) {
        this.host = host;
    }

    public Message query(Message query, Integer timeout) throws IOException {
        // Send the query
        // Use GET, so base64url encode the query wire bytes
        String encodedQuery = Base64.getUrlEncoder().withoutPadding().encodeToString(query.toWire());
        Message response = null;
        response = makeGetRequest(encodedQuery, timeout);

        // TODO Handle timeouts!

        return response;
    }

    private Message makeGetRequest(String encodedQuery, Integer timeout) throws IOException {
        // Make an HTTPS GET request to the host, using RFC8484 format
        HttpsURLConnection con = null;
        try {

            URL myurl = new URL(host + "?dns=" + encodedQuery);
            con = (HttpsURLConnection) myurl.openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("GET");
            con.setConnectTimeout(timeout);
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("content-type", "application/dns-message");

            byte[] readBytes = new byte[65535];
            con.getInputStream().read(readBytes);

//            String returnContentType = con.getContentType();

            String cache_control = con.getHeaderField("cache-control");
            String maxAgeString = cache_control.replace("max-age=", "");
            Integer maxAge = new Integer(maxAgeString);
            // TODO Do something with max-age!
            System.out.println("max-age=" + maxAge);

            // Make the DNS response message
            Message response = new Message(readBytes);

            return response;

        } finally {

            con.disconnect();
        }
    }
}
