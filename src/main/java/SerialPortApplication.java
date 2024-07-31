import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

import java.io.IOException;

public final class SerialPortApplication {
	
	public static void main(String[] args) throws IOException {
		SerialPort[] serialPorts = SerialPort.getCommPorts();//查找所有串口
		SerialPort serialPort;
		
		for (SerialPort port : serialPorts) {
			System.out.println("Port:" + port.getSystemPortName());//打印串口名称，如COM4
			System.out.println("PortDesc:" + port.getPortDescription());//打印串口类型，如USB Serial
			System.out.println("PortDesc:" + port.getDescriptivePortName());//打印串口的完整类型，如USB-SERIAL CH340(COM4)
		}
		
		if (serialPorts.length > 0) {
			// 获取到第一个串口
			serialPort = serialPorts[0];
			// 设置超时
			serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING | SerialPort.TIMEOUT_WRITE_BLOCKING, 1000, 1000);
			// 设置串口的控制流，可以设置为disabled，或者CTS, RTS/CTS, DSR, DTR/DSR, Xon, Xoff, Xon/Xoff等
			serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);
			// 一次性设置所有的串口参数，第一个参数为波特率，默认9600；第二个参数为每一位的大小，默认8，可以输入5到8之间的值；第三个参数为停止位大小，只接受内置常量，可以选择(ONE_STOP_BIT, ONE_POINT_FIVE_STOP_BITS, TWO_STOP_BITS)；第四位为校验位，同样只接受内置常量，可以选择 NO_PARITY, EVEN_PARITY, ODD_PARITY, MARK_PARITY,SPACE_PARITY。
			serialPort.setComPortParameters(115200, 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
			// 检测串口状态
			if (!serialPort.isOpen()) {
				serialPort.openPort();//判断串口是否打开，如果没打开，就打开串口。打开串口的函数会返回一个boolean值，用于表明串口是否成功打开了
				String writeData = "hello world";//要发送的字符串
				byte[] bytes = writeData.getBytes();//将字符串转换为字节数组
				serialPort.writeBytes(bytes, bytes.length);//将字节数组全部写入串口
				try {
					Thread.sleep(100);//休眠0.1秒，等待下位机返回数据。如果不休眠直接读取，有可能无法成功读到数据
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				String readData = "";
				while (serialPort.bytesAvailable() > 0) {//循环读取所有的返回数据。如果可读取数据长度为0或-1，则停止读取
					byte[] newData = new byte[serialPort.bytesAvailable()];//创建一个字节数组，长度为可读取的字节长度
					int numRead = serialPort.readBytes(newData, newData.length);//将串口中可读取的数据读入字节数组，返回值为本次读取到的字节长度
					String newDataString = new String(newData);//将新数据转为字符串
					readData = readData + newDataString;//组合字符串
					try {
						Thread.sleep(20);//休眠0.02秒，等待下位机传送数据到串口。如果不休眠，直接再次使用port.bytesAvailable()函数会因为下位机还没有返回数据而返回-1，并跳出循环导致数据没读完。休眠时间可以自行调试，时间越长，单次读取到的数据越多。
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
				System.out.println("readString:" + readData);
				
				if (serialPort.isOpen()) {
					serialPort.addDataListener(new SerialPortDataListener() {//添加监听器。由于该监听器有两个函数，无法使用Lambda表达式
						@Override
						public int getListeningEvents() {
							// TODO Auto-generated method stub
							return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;//返回要监听的事件类型，以供回调函数使用。可发回的事件包括：SerialPort.LISTENING_EVENT_DATA_AVAILABLE，SerialPort.LISTENING_EVENT_DATA_WRITTEN,SerialPort.LISTENING_EVENT_DATA_RECEIVED。分别对应有数据在串口（不论是读的还是写的），有数据写入串口，从串口读取数据。如果AVAILABLE和RECEIVED同时被监听，优先触发RECEIVED
						}
						
						@Override
						public void serialEvent(SerialPortEvent event) {//事件处理函数
							// TODO Auto-generated method stub
							String data = "";
							if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
								return;//判断事件的类型
							}
							while (serialPort.bytesAvailable() != 0) {
								byte[] newData = new byte[serialPort.bytesAvailable()];
								int numRead = serialPort.readBytes(newData, newData.length);
								String newDataString = new String(newData);
								data = data + newDataString;
								try {
									Thread.sleep(20);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}//同样使用循环读取法读取所有数据
							//由于这里是监听函数，所以也可以不使用循环读取法，在监听器外创建一个全局变量，然后将每次读取到的数据添加到全局变量里
						}
					});
				}
				
				
			}
			// 关闭串口。该函数同样会返回一个boolean值，表明串口是否成功关闭
			serialPort.closePort();
		}
	}
}

