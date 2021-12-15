package waeng.bootrestapi.common;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author waeng
 * @since 2021/12/15
 */
@MappedJdbcTypes(JdbcType.INTEGER)
@MappedTypes(Date.class)
public class ImplicitHandler implements TypeHandler<Date> {

    @Override
    public void setParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        ps.setInt(i, (int) (parameter.getTime() / 1000));
    }

    @Override
    public Date getResult(ResultSet rs, String columnName) throws SQLException {
        return new Date(((long) rs.getInt(columnName)) * 1000);
    }

    @Override
    public Date getResult(ResultSet rs, int columnIndex) throws SQLException {
        return new Date(((long) rs.getInt(columnIndex)) * 1000);
    }

    @Override
    public Date getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return new Date(((long) cs.getInt(columnIndex)) * 1000);
    }
}