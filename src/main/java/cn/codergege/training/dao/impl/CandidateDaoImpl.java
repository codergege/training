package cn.codergege.training.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cn.codergege.training.dao.CandidateDao;
import cn.codergege.training.domain.Candidate;

@Repository("candidateDao")
public class CandidateDaoImpl implements CandidateDao {
	@Resource
	private SessionFactory sessionFactory;
	private Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	@Override
	public List<Candidate> getByPage(Integer page, Integer rows, String sort, String order, Candidate candidate, Double credit1, Double credit2, Integer cid, Integer tid) {
		String hql = "from Candidate where 1=1 ";
		if(tid != null){
			hql = "from Candidate as c inner join fetch c.trainings as t where t.tid = " + tid; 
		}
		if(cid != null){
			hql += " and cid = " + cid;
		}
		if(candidate.getName() != null){
			hql += " and name like '%" + candidate.getName() + "%'";
		}
		if(candidate.getGender() != null){
			hql += " and gender like '%" + candidate.getGender() + "%'";
		}
		if(candidate.getUnit() != null){
			hql += " and unit like '%" + candidate.getUnit() + "%'";
		}
		if(candidate.getBzlx() != null){
			hql += " and bzlx like '%" + candidate.getBzlx() + "%'";
		}
		if(candidate.getState() != null){
			hql += " and state like '%" + candidate.getState() + "%'";
		}
		
		if(sort != null && order != null){
			hql += "order by " + sort + " " + order;
		}
		if(page == null){
			return getSession().createQuery(hql).list();
		}
		List<Candidate> candidates = getSession().createQuery(hql).setFirstResult((page-1)*rows).setMaxResults(rows).list();
		return candidates;
	}
	@Override
	public Integer getTotal() {
		String hql = "select count(*) from Candidate";
		Integer total = ((Number)getSession().createQuery(hql).uniqueResult()).intValue();
		return total;
	}
	@Override
	public int add(Candidate candidate) {
		int retVal = 0;
		retVal = (int) getSession().save(candidate);
		System.out.println(retVal);
		return retVal;
		
	}

	@Override
	public Candidate getCandidate(Integer cid) {
		return (Candidate) getSession().get(Candidate.class, cid);
	}

	@Override
	public void update(Candidate candidate) {
		getSession().update(candidate);
	}

	@Override
	public void delete(String cids) {
		String hql = "delete from Candidate where cid in (" + cids + ")";
		getSession().createQuery(hql).executeUpdate();
	}
	@Override
	public Candidate getCandidate(String name) {
		System.out.println(name.trim());
		String hql = "from Candidate where name like ?";
		return (Candidate) getSession().createQuery(hql).setString(0, name.trim()).uniqueResult();
	}
	@Override
	public List<Candidate> getCandidates(String cids) {
		String hql = "from Candidate where cid in (" + cids + ")";
		return getSession().createQuery(hql).list();
	}
}
