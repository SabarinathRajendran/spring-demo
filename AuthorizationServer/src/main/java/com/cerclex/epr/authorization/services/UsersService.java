package com.cerclex.epr.authorization.services;
import com.cerclex.epr.authorization.dao.UserRepository;
import com.cerclex.epr.authorization.dtos.User;
import com.cerclex.epr.authorization.utils.AppConstants;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UsersService {

	private final UserRepository userRepository;

	public UsersService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public List<User> getUserList() {
		List<User> UserLst = new ArrayList<User>();
		userRepository.findAll().forEach(user -> UserLst.add(user));
		return UserLst;
	}
	
	@Transactional(rollbackFor = {Exception.class})
	public User encryptAndSave(User user)   
	{  
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(AppConstants.bCryptEncoderStrength, new SecureRandom());
		String secPwd = encoder.encode(user.getPassword());
		user.setPassword(secPwd);
		return saveOrUpdate(user);  
	}  
	
	public User encryptAndUpdate(User user)   
	{  
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(AppConstants.bCryptEncoderStrength, new SecureRandom());
		String secPwd = encoder.encode(user.getPassword());
		user.setPassword(secPwd);
		return saveOrUpdate(user);  
	} 
	
	@Caching(evict = {@CacheEvict(value="user", key = "#user.getId()", allEntries = true)}, put = {@CachePut(value = "user", key = "#user.getId()", unless="#result == null")})
	public User saveOrUpdate(User user)   
	{  
		return userRepository.save(user);  
	}  

	@CacheEvict(value="user", key = "#user.getId()", allEntries = true)
	public void deleteEntry(User user) {
		userRepository.delete(user);
	}
	
	public User getUserByUsrNameAndPwd(User user) {
		return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
	}
	
	public Optional<User> getByUsrName(String userName) {
		return userRepository.findByUsername(userName);
	}
	
	@Cacheable(value="user", key = "#id", unless="#result == null")
	public Optional<User> findById(String id) {
		return userRepository.findById(Long.parseLong(id));
	}
	
	public List<User> findUserByStr(String searchStr) {
		List<User> result = new ArrayList<User>();

		HashSet<User> tmpresult = new HashSet<User>();
		
		HashSet<User> artName = userRepository.findByUsernameContaining(searchStr);
		HashSet<User> artPreview = userRepository.findByFullnameContaining(searchStr);
		
		if(artName!=null && artName.size()>0) {
			tmpresult.addAll(artName);
		}
		if(artPreview!=null && artPreview.size()>0) {
			tmpresult.addAll(artPreview);
		}
		
		result = new ArrayList<User>(tmpresult);
		System.out.println(result.size());
		
		return result;
	}
	
}
