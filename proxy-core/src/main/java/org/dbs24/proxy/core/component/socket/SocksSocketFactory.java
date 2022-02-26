package org.dbs24.proxy.core.component.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.net.SocketFactory;

import io.jsonwebtoken.lang.Objects;
import org.dbs24.proxy.core.entity.domain.Proxy;
import sockslib.client.Socks5;
import sockslib.client.SocksProxy;
import sockslib.client.SocksSocket;
import sockslib.common.UsernamePasswordCredentials;

public class SocksSocketFactory extends SocketFactory{
	private final SocksProxy proxy;
	private final int socketTimeout;
	
	public SocksSocketFactory(SocksProxy proxy, int socketTimeout) {
		this.proxy = proxy;
		this.socketTimeout = socketTimeout;
	}

	public SocksSocketFactory(Proxy proxy,
							  Integer socketTimeout) {

		Integer port = proxy.getSocksPort() == null ? proxy.getPort() : proxy.getSocksPort();
		Socks5 sslSocks5 = new Socks5(new InetSocketAddress(proxy.getUrl(), port));
		sslSocks5.setCredentials(new UsernamePasswordCredentials(
				Objects.nullSafeToString(proxy.getLogin()),
				Objects.nullSafeToString(proxy.getPass()))
		);

		this.proxy = sslSocks5;
		this.socketTimeout = socketTimeout;
	}
	
	public Socket createSocket() throws IOException {
		SocksSocket socket = new SocksSocket(proxy);
		socket.setSoTimeout(socketTimeout);
		return socket;
	}

	public Socket createSocket(String host, int port)
			throws IOException, UnknownHostException {
		SocksSocket socket = new SocksSocket(proxy, host, port);
		socket.setSoTimeout(socketTimeout);
		return socket;
	}

	public Socket createSocket(String host, int port, InetAddress localHost,
			int localPort) throws IOException, UnknownHostException {
		SocksSocket socket = new SocksSocket(proxy, host, port);
		socket.setSoTimeout(socketTimeout);
		return socket;
	}

	public Socket createSocket(InetAddress host, int port) throws IOException {
		SocksSocket socket = new SocksSocket(proxy, host, port);
		socket.setSoTimeout(socketTimeout);
		return socket;
	}

	public Socket createSocket(InetAddress address, int port,
			InetAddress localAddress, int localPort) throws IOException {
		SocksSocket socket = new SocksSocket(proxy, address, port);
		socket.setSoTimeout(socketTimeout);
		return socket;
	}
}
