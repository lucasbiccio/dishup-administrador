package br.com.dishup.administrador.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.Transaction;
import br.com.dishup.administrador.bean.ProposalBean;
import br.com.dishup.administrador.bean.ProposalItemBean;
import br.com.dishup.core.codedata.ProposalStatusCD;
import br.com.dishup.core.codedata.RestaurantStatusCD;
import br.com.dishup.core.codedata.UserStatusCD;
import br.com.dishup.core.codedata.UserTypeCD;
import br.com.dishup.core.entity.RestaurantAddressEntity;
import br.com.dishup.core.entity.RestaurantBasicInfoEntity;
import br.com.dishup.core.entity.RestaurantEntity;
import br.com.dishup.core.entity.RestaurantProposalEntity;
import br.com.dishup.core.entity.RestaurantResponsibleEntity;
import br.com.dishup.core.entity.RestaurantUserGroupEntity;
import br.com.dishup.core.entity.UserAdministratorEntity;
import br.com.dishup.core.entity.UserRestaurantEntity;
import br.com.dishup.core.exception.DishUpException;
import br.com.dishup.core.exception.FieldValidatorException;
import br.com.dishup.core.exception.ProposalNotFoundException;
import br.com.dishup.core.exception.RestaurantNotFoundException;
import br.com.dishup.core.exception.StatusProposalNotFoundException;
import br.com.dishup.core.exception.StatusRestaurantNotFoundException;
import br.com.dishup.core.exception.StatusUserNotFoundException;
import br.com.dishup.core.exception.UserAdministratorNotFoundException;
import br.com.dishup.core.exception.UserRestaurantNotFoundException;
import br.com.dishup.core.exception.UserTypeNotFoundException;
import br.com.dishup.core.persistence.HibernateUtil;
import br.com.dishup.core.persistence.ProposalStatusDAO;
import br.com.dishup.core.persistence.RestaurantAddressDAO;
import br.com.dishup.core.persistence.RestaurantBasicInfoDAO;
import br.com.dishup.core.persistence.RestaurantDAO;
import br.com.dishup.core.persistence.RestaurantProposalDAO;
import br.com.dishup.core.persistence.RestaurantResponsibleDAO;
import br.com.dishup.core.persistence.RestaurantStatusDAO;
import br.com.dishup.core.persistence.RestaurantUserGroupDAO;
import br.com.dishup.core.persistence.UserAdministratorDAO;
import br.com.dishup.core.persistence.UserRestaurantDAO;
import br.com.dishup.core.persistence.UserStatusDAO;
import br.com.dishup.core.persistence.UserTypeDAO;
import br.com.dishup.core.util.FieldValidatorUtil;

public class ProposalManagerService {

	public void aprooveProposal(int idProposal, int idUserAdministrator) throws FieldValidatorException, DishUpException{
		if(!FieldValidatorUtil.isValidId(idProposal))
			throw new FieldValidatorException("ID DA PROPOSTA INVALIDO");
		if(!FieldValidatorUtil.isValidId(idUserAdministrator))
			throw new FieldValidatorException("ID DO USUARIO ADMINISTRADOR INVALIDO");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		RestaurantProposalDAO proposalDAO = new RestaurantProposalDAO();
		ProposalStatusDAO proposalStatusDAO = new ProposalStatusDAO();
		UserAdministratorDAO userAdministratorDAO = new UserAdministratorDAO();
		RestaurantDAO restaurantDAO = new RestaurantDAO();
		RestaurantResponsibleDAO restaurantResponsibleDAO = new RestaurantResponsibleDAO();
		RestaurantAddressDAO restaurantAddressDAO = new RestaurantAddressDAO();
		RestaurantUserGroupDAO restaurantUserGroupDAO = new RestaurantUserGroupDAO();
		RestaurantStatusDAO restaurantStatusDAO = new RestaurantStatusDAO();
		UserRestaurantDAO userRestaurantDAO = new UserRestaurantDAO();
		UserStatusDAO userStatusDAO = new UserStatusDAO();
		UserTypeDAO userTypeDAO = new UserTypeDAO();
		RestaurantBasicInfoDAO restaurantBasicInfoDAO = new RestaurantBasicInfoDAO();
		
		try{
			UserAdministratorEntity userAproover = userAdministratorDAO.selectById(session, idUserAdministrator);
			RestaurantProposalEntity proposal = proposalDAO.selectById(session, idProposal);
			
			if(proposal.getStatus().getId() == ProposalStatusCD.PENDING.getId()){
				
				proposal.setUserAproover(userAproover);
				proposal.setStatus(proposalStatusDAO.selectById(session, ProposalStatusCD.APROOVED.getId()));
				proposal.setDateAproove(new Date());
				proposalDAO.update(session, proposal);
				
				RestaurantEntity restaurantEntity = new RestaurantEntity(0, proposal.getCNPJroot(), 
						proposal.getCNPJfilial(), proposal.getCNPJcontrol(), proposal.getSocialReason(), proposal.getSite(), 
						proposal.getDDDtelephone(), proposal.getTelephone(), new Date(), restaurantStatusDAO.selectById(session, RestaurantStatusCD.ACTIVE.getId()));
				restaurantDAO.insert(session, restaurantEntity);
				
				RestaurantEntity restaurant = restaurantDAO.selectByCNPJ(session, proposal.getCNPJroot(), proposal.getCNPJfilial(), proposal.getCNPJcontrol());
				
				RestaurantResponsibleEntity responsible = new RestaurantResponsibleEntity(
						restaurant, proposal.getCPFroot(), proposal.getCPFcontrol(), proposal.getResponsibleName(), 
						proposal.getResponsibleEmployment(), proposal.getResponsibleDDDtelephone(), proposal.getResponsibleTelephone());
				restaurantResponsibleDAO.insert(session, responsible);
				
				RestaurantAddressEntity restaurantAddress = new RestaurantAddressEntity(restaurant, proposal.getCEP().substring(0, 5), 
						proposal.getCEP().substring(5, 8), proposal.getAddress(), proposal.getAddressNumber(), proposal.getNeighborhood(), 
						proposal.getCountry(), proposal.getState(), proposal.getCity());
				restaurantAddressDAO.insert(session, restaurantAddress);
				
				RestaurantBasicInfoEntity restaurantBasicInfo = new RestaurantBasicInfoEntity(restaurant, null, null, null, proposal.getCulinaryType());
				restaurantBasicInfoDAO.insert(session, restaurantBasicInfo);
				
				UserRestaurantEntity userRestaurant;
				try{
					userRestaurant = userRestaurantDAO.selectByEmail(session, proposal.getEmail());
				}catch(UserRestaurantNotFoundException e){
					userRestaurant = new UserRestaurantEntity(0, proposal.getEmail(), "NEED TO CHANGE THE PASSWORD", 
							userStatusDAO.selectById(session, UserStatusCD.PENDING_CHANGE_PASSWORD.getId()), new Date());
					userRestaurantDAO.insert(session, userRestaurant);
				}
				
				RestaurantUserGroupEntity group = new RestaurantUserGroupEntity(restaurant, userRestaurant, 
						userTypeDAO.selectById(session, UserTypeCD.RESTAURANT_MASTER.getId()));
				restaurantUserGroupDAO.insert(session, group);
				transaction.commit();
			}else
				throw new DishUpException("STATUS DA PROPOSTA NAO PERMITE APROVACAO");
		} catch (UserAdministratorNotFoundException e) {
			throw new DishUpException("USUARIO APROVADOR NAO E ADMINISTRADOR");
		} catch (ProposalNotFoundException e) {
			throw new DishUpException("PROPOSTA NAO ENCONTRADA");
		} catch (StatusProposalNotFoundException e) {
			e.printStackTrace();
			throw new DishUpException("SERVICO TEMPORARIAMENTE INDISPONIVEL");
		} catch (StatusRestaurantNotFoundException e) {
			e.printStackTrace();
			throw new DishUpException("SERVICO TEMPORARIAMENTE INDISPONIVEL");
		} catch (RestaurantNotFoundException e) {
			e.printStackTrace();
			throw new DishUpException("SERVICO TEMPORARIAMENTE INDISPONIVEL");
		} catch (StatusUserNotFoundException e) {
			e.printStackTrace();
			throw new DishUpException("SERVICO TEMPORARIAMENTE INDISPONIVEL");
		} catch (UserTypeNotFoundException e) {
			e.printStackTrace();
			throw new DishUpException("SERVICO TEMPORARIAMENTE INDISPONIVEL");
		} finally{
			session.close();
		}
	}
	
	public void rejectProposal(int idProposal, int idUserAdministrator) throws FieldValidatorException, DishUpException{
		if(!FieldValidatorUtil.isValidId(idProposal))
			throw new FieldValidatorException("ID DA PROPOSTA INVALIDO");
		if(!FieldValidatorUtil.isValidId(idUserAdministrator))
			throw new FieldValidatorException("ID DO USUARIO ADMINISTRADOR INVALIDO");
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		RestaurantProposalDAO proposalDAO = new RestaurantProposalDAO();
		ProposalStatusDAO statusProposalDAO = new ProposalStatusDAO();
		UserAdministratorDAO userAdministratorDAO = new UserAdministratorDAO();
		
		try{
			UserAdministratorEntity userAproover = userAdministratorDAO.selectById(session, idUserAdministrator);
			RestaurantProposalEntity proposal = proposalDAO.selectById(session, idProposal);
			if(proposal.getStatus().getId() == ProposalStatusCD.PENDING.getId()){
				proposal.setStatus(statusProposalDAO.selectById(session, ProposalStatusCD.REJECTED.getId()));
				proposal.setUserAproover(userAproover);
				proposal.setDateAproove(new Date());
				proposalDAO.update(session, proposal);
				transaction.commit();
			}else
				throw new DishUpException("STATUS DA PROPOSTA NAO PERMITE REJEICAO");
		}catch (ProposalNotFoundException e) {
			throw new DishUpException("PROPOSTA NAO ENCONTRADA");
		}catch (StatusProposalNotFoundException e) {
			e.printStackTrace();
			throw new DishUpException("SERVICO TEMPORARIAMENTE INDISPONIVEL");
		}catch (UserAdministratorNotFoundException e) {
			throw new DishUpException("USUARIO APROVADOR NAO E ADMINISTRADOR");
		}finally{
			session.close();
		}
	}
	
	public ArrayList<ProposalItemBean> listPendingProposal() throws DishUpException{
		ArrayList<ProposalItemBean> listPendingProposal = new ArrayList<ProposalItemBean>();
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestaurantProposalDAO proposalDAO = new RestaurantProposalDAO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try{
			ArrayList<RestaurantProposalEntity> listProposalEntity = 
					(ArrayList<RestaurantProposalEntity>) proposalDAO.selectByStatus(session, ProposalStatusCD.PENDING.getId());
			for (RestaurantProposalEntity proposal : listProposalEntity) {
				listPendingProposal.add(new ProposalItemBean(
						proposal.getId(), 
						proposal.getCNPJroot()+"/"+proposal.getCNPJfilial()+"/"+proposal.getCNPJcontrol(),
						proposal.getSocialReason(),
						dateFormat.format(proposal.getDateInclusion()),
						proposal.getStatus().getName()));
			}
		}catch (ProposalNotFoundException e) {
			throw new DishUpException("NAO HA PROPOSTAS PENDENTES DE APROVACAO");
		}finally{
			session.close();
		}
		return listPendingProposal;
	}

	public ProposalBean detailProposal(int idProposal) throws FieldValidatorException, DishUpException{
		if(!FieldValidatorUtil.isValidId(idProposal))
			throw new FieldValidatorException("ID DA PROPOSTA INFORMADA ESTA INVALIDO");
		Session session = HibernateUtil.getSessionFactory().openSession();
		RestaurantProposalDAO propostaDAO = new RestaurantProposalDAO();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		ProposalBean proposalBean;
		try{
			RestaurantProposalEntity proposalEntity = propostaDAO.selectById(session, idProposal);
			String userAprooverString;
			if(proposalEntity.getUserAproover() != null)
				userAprooverString = proposalEntity.getUserAproover().getEmail();
			else
				userAprooverString = "N/A";
			String aprooveDateString;
			if(proposalEntity.getDateAproove() != null)
				aprooveDateString = dateFormat.format(proposalEntity.getDateAproove());
			else
				aprooveDateString = "N/A";
			proposalBean = new ProposalBean(
					proposalEntity.getId(), 
					dateFormat.format(proposalEntity.getDateInclusion()),
					proposalEntity.getStatus().getId()+"-"+proposalEntity.getStatus().getName(), 
					userAprooverString, 
					aprooveDateString, 
					proposalEntity.getEmail(), 
					proposalEntity.getCNPJroot().trim()+"/"+proposalEntity.getCNPJfilial()+"/"+proposalEntity.getCNPJcontrol(), 
					proposalEntity.getSocialReason(), 
					proposalEntity.getCEP(), 
					proposalEntity.getAddress(), 
					proposalEntity.getAddressNumber(), 
					proposalEntity.getAddressComplement(),
					proposalEntity.getNeighborhood(), 
					proposalEntity.getCity().getName(), 
					proposalEntity.getState().getName(),
					proposalEntity.getCountry().getName(), 
					proposalEntity.getDDDtelephone(), 
					proposalEntity.getTelephone().trim(), 
					proposalEntity.getSite(), 
					proposalEntity.getCulinaryType().getName(), 
					proposalEntity.getCPFroot()+"-"+proposalEntity.getCPFcontrol(), 
					proposalEntity.getResponsibleName(), 
					proposalEntity.getResponsibleEmployment().getName(),
					proposalEntity.getResponsibleDDDtelephone(), 
					proposalEntity.getResponsibleTelephone().trim());
		}catch (ProposalNotFoundException e) {
			throw new DishUpException("PROPOSTA NAO ENCONTRADA");
		}finally{
			session.close();
		}
		return proposalBean;
	}
}