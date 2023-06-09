package MyApp;

	import java.awt.event.ActionEvent;
	import java.awt.event.ActionListener;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.util.function.ObjLongConsumer;

	import javax.swing.JButton;
	import javax.swing.JFrame;
	import javax.swing.JLabel;
	import javax.swing.JTextField;

	import com.mysql.cj.protocol.PacketSentTimeHolder;
	import com.mysql.cj.protocol.a.result.ResultsetRowsStatic;
	import com.mysql.cj.x.protobuf.MysqlxCrud.Delete;
	import com.mysql.cj.x.protobuf.MysqlxCrud.Update;

	public class Swing implements ActionListener {
		JLabel l1, l2, l3, l4, l5;
		JTextField t1, t2, t3, t4, t5;
		JButton b1, b2, b3, b4;

		Swing() {
			JFrame frame = new JFrame("MyApp");
			frame.setVisible(true);
			frame.setSize(700, 500);
			frame.setLayout(null);

			l1 = new JLabel("Id : ");
			l1.setBounds(100, 100, 120, 20);
			frame.add(l1);
			l2 = new JLabel("Name : ");
			l2.setBounds(100, 130, 120, 20);
			frame.add(l2);
			l3 = new JLabel("Contact : ");
			l3.setBounds(100, 160, 120, 20);
			frame.add(l3);
			l4 = new JLabel("Email : ");
			l4.setBounds(100, 190, 120, 20);
			frame.add(l4);
			l5 = new JLabel("Address : ");
			l5.setBounds(100, 220, 120, 20);
			frame.add(l5);

			t1 = new JTextField();
			t1.setBounds(200, 100, 120, 20);
			frame.add(t1);
			t2 = new JTextField();
			t2.setBounds(200, 130, 120, 20);
			frame.add(t2);
			t3 = new JTextField();
			t3.setBounds(200, 160, 120, 20);
			frame.add(t3);
			t4 = new JTextField();
			t4.setBounds(200, 190, 120, 20);
			frame.add(t4);
			t5 = new JTextField();
			t5.setBounds(200, 220, 120, 20);
			frame.add(t5);

			b1 = new JButton("INSERT");
			b1.setBounds(100, 300, 120, 20);
			frame.add(b1);
			b2 = new JButton("SEARCH");
			b2.setBounds(250, 300, 120, 20);
			frame.add(b2);
			b3 = new JButton("UPDATE");
			b3.setBounds(100, 330, 120, 20);
			frame.add(b3);
			b4 = new JButton("DELETE");
			b4.setBounds(250, 330, 120, 20);
			frame.add(b4);

			b1.addActionListener(this);
			b2.addActionListener(this);
			b3.addActionListener(this);
			b4.addActionListener(this);
		}

		public static void main(String[] args) {
			new Swing();
		}
		
		
		public static Connection createConnection() {
			Connection connection = null;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/myapp", "root", "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return connection;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == b1) {
				System.out.println("insert button clicked");
				int id = Integer.parseInt(t1.getText());
				String nameString = t2.getText();
				long contact = Long.parseLong(t3.getText());
				String emailString = t4.getText();
				String addressString = t5.getText();
				System.out.println(id+nameString+contact+emailString+addressString);
				try {
					Connection connection = Swing.createConnection();
					String sql= "insert into data(id,name,contact,email,address) values(?,?,?,?,?)";
					PreparedStatement pst = connection.prepareStatement(sql);
					pst.setInt(1, id);
					pst.setString(2, nameString);
					pst.setLong(3, contact);
					pst.setString(4, emailString);
					pst.setString(5, addressString);
//					DML->insert/Update/Delete ===> executeUpdate();
//					DQL->select ==> executeQuery();
					pst.executeUpdate();
					System.out.println("data stored successfully");
					t1.setText("");
					t2.setText("");
					t3.setText("");
					t4.setText("");
					t5.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (e.getSource() == b2) {
				System.out.println("search button clicked");
				int id  = Integer.parseInt(t1.getText());
				try {
					Connection connection = Swing.createConnection();
					String sql = "select * from data where id=?";
					PreparedStatement pst = connection.prepareStatement(sql);
					pst.setInt(1, id);
					ResultSet rs = pst.executeQuery();
					if(rs.next()) {
						t1.setText(String.valueOf(rs.getInt("id")));
						t2.setText(rs.getString("name"));
						t3.setText(String.valueOf(rs.getLong("contact")));
						t4.setText(rs.getString("email"));
						t5.setText(rs.getString("address"));
					}
					else {
						System.out.println("data not found");
						t1.setText("");
						t2.setText("");
						t3.setText("");
						t4.setText("");
						t5.setText("");
					}
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			} else if (e.getSource() == b3) {
				System.out.println("update button clicked");
				int id = Integer.parseInt(t1.getText());
				String nameString = t2.getText();
				long contact = Long.parseLong(t3.getText());
				String emailString = t4.getText();
				String addressString = t5.getText();
				try {
					Connection connection = Swing.createConnection();
					String sql = "update data set name=?,contact=?,email=?,address=? where id=?";
					PreparedStatement pst = connection.prepareStatement(sql);
					pst.setString(1, nameString);
					pst.setLong(2, contact);
					pst.setString(3, emailString);
					pst.setString(4, addressString);
					pst.setInt(5, id);
					pst.executeUpdate();
					System.out.println("data updated");
					t1.setText("");
					t2.setText("");
					t3.setText("");
					t4.setText("");
					t5.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
				
			} else if (e.getSource() == b4) {
				System.out.println("delete button clicked");
				int id = Integer.parseInt(t1.getText());
				try {
					Connection connection = Swing.createConnection();
					String sql = "delete from data where id=?";
					PreparedStatement pst = connection.prepareStatement(sql);
					pst.setInt(1, id);
					pst.executeUpdate();
					System.out.println("data deleted");
					t1.setText("");
					t2.setText("");
					t3.setText("");
					t4.setText("");
					t5.setText("");
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	}
	


