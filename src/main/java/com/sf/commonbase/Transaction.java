package com.sf.commonbase;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * 数据库事务管理类
 * @author tuzhaoliang
 * @date 2018年5月5日
 */
@Component("transaction")
public class Transaction {
	
	@Resource(name="transactionManager")
	private DataSourceTransactionManager txManager;
	
	/**
	 * 开启事务，一旦开启事务之后，操作表增删改，一定要调用commit，或者rollBack，不然表被死锁!
	 * @return
	 */
	public TransactionStatus beginTansaction(){
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status = txManager.getTransaction(def);
		return status;
	}
	
	public void commit(TransactionStatus status){
		txManager.commit(status);
	}
	
	public void rollback(TransactionStatus status){
		txManager.rollback(status);
	}
}
