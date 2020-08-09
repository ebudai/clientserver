/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.clientserver;

import com.caucho.hessian.io.HessianOutput;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.rptools.clientserver.hessian.AbstractMethodHandler;
import net.rptools.clientserver.hessian.client.ClientConnection;
import net.rptools.clientserver.hessian.server.ServerConnection;

/**
 * @author drice
 *     <p>TODO To change the template for this generated type comment go to Window - Preferences -
 *     Java - Code Style - Code Templates
 */
public class Test01 {

  public static void main(String[] args) throws Exception {
    ServerConnection server = new ServerConnection(4444);
    server.addMessageHandler(new ServerHandler());

    ClientConnection client = new ClientConnection("192.168.1.102", 4444, "Testing");
    client.addMessageHandler(new ClientHandler());

    for (int i = 0; i < 1000; i++) {
      if (i % 3 == 0) {
        client.callMethod("fromClient", "arg1", "arg2");
      }
      server.broadcastCallMethod("fromServer", "arg1");
      Thread.sleep(1000);
    }

    client.close();
    server.close();
  }

  private static byte[] getOutput(String method) throws IOException {
    ByteArrayOutputStream bout = new ByteArrayOutputStream();
    HessianOutput hout = new HessianOutput(bout);

    hout.call(method, new Object[0]);

    return bout.toByteArray();
  }

  private static class ServerHandler extends AbstractMethodHandler {

    public void handleMethod(String id, String method, Object... parameters) {
      System.out.println(
          "Server received: " + method + " from " + id + " args=" + parameters.length);
      for (Object param : parameters) {
        System.out.println("\t" + param);
      }
    }
  }

  private static class ClientHandler extends AbstractMethodHandler {
    public void handleMethod(String id, String method, Object... parameters) {
      System.out.println(
          "Client received: " + method + " from " + id + " args=" + parameters.length);
      for (Object param : parameters) {
        System.out.println("\t" + param);
      }
    }
  }
}
