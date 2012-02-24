package deng.quartz.bonecp;

import java.sql.Connection;
import java.sql.SQLException;
import org.quartz.utils.ConnectionProvider;
import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPDataSource;

public class BoneCpConnectionProvider extends BoneCPDataSource implements ConnectionProvider {
	
	private static final long serialVersionUID = 1L;
	private BoneCP boneCp;
	
	@Override
	public Connection getConnection() throws SQLException {
		if (boneCp == null)
			boneCp = new BoneCP(this);
		return boneCp.getConnection();
	}

	@Override
	public void shutdown() throws SQLException {
		boneCp.shutdown();
	}

}
