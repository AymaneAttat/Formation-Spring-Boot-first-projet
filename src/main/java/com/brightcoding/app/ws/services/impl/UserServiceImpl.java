package com.brightcoding.app.ws.services.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.brightcoding.app.ws.entities.UserEntity;
import com.brightcoding.app.ws.repositories.UserRepository;
import com.brightcoding.app.ws.responses.UserResponse;
import com.brightcoding.app.ws.services.UserService;
import com.brightcoding.app.ws.shared.Utils;
import com.brightcoding.app.ws.shared.dto.AddressDto;
import com.brightcoding.app.ws.shared.dto.UserDto;

@Service
public class UserServiceImpl implements UserService {
//@Resource
	@Autowired //pour considérer userRepository comme une instanciation injection de dépendance il faut utiliser @autowired
	UserRepository userRepository;
	
	@Autowired
	Utils util;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		UserEntity checkUser = userRepository.findByEmail(userDto.getEmail());
		if(checkUser != null) throw new RuntimeException("User Already Exists !");
		
		//UserEntity userEntity = new UserEntity();
		//BeanUtils.copyProperties(userDto, userEntity);
		
		for(int i=0; i<userDto.getAddresses().size(); i++) {
			AddressDto address = userDto.getAddresses().get(i);
			address.setUser(userDto);
			address.setAddressId(util.generateStringId(30));
			userDto.getAddresses().set(i, address);
		}
		
		userDto.getContact().setContactId(util.generateStringId(30));
		userDto.getContact().setUser(userDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);
		
		userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
		userEntity.setUserId(util.generateStringId(32));
		
		UserEntity newUser = userRepository.save(userEntity);//persister un objet
		
		//mais le return de cette fonction est UserDto donc
		//UserDto userDTO = new UserDto();
		//BeanUtils.copyProperties(newUser, userDTO);
		UserDto userDTO = modelMapper.map(newUser, UserDto.class);
		
		return userDTO;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		UserEntity userEntity = userRepository.findByEmail(email);
		if(userEntity == null) throw new UsernameNotFoundException(email);
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public UserDto getUserByUserId(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException(userId);
		
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userEntity, userDto);
		return userDto;
	}

	@Override
	public UserDto updateUser(String userId, UserDto userDto) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException(userId);
		
		userEntity.setFirstName(userDto.getFirstName());
		userEntity.setLastName(userDto.getLastName());
		
		UserEntity userUpdated = userRepository.save(userEntity);
		
		UserDto user = new UserDto();
		BeanUtils.copyProperties(userUpdated, user);
		return user;
	}

	@Override
	public void deleteUser(String userId) {
		UserEntity userEntity = userRepository.findByUserId(userId);
		if(userEntity == null) throw new UsernameNotFoundException(userId);
		
		userRepository.delete(userEntity);
	}

	@Override
	public List<UserDto> getUsers(int page, int limit, String search, int status) {
		if(page > 0) page -= 1;

		List<UserDto> usersDto = new ArrayList<>();
		//PageRequest pageableRequest = PageRequest.of(page, limit);
		Pageable  pageableRequest = PageRequest.of(page, limit);
		//Page<UserEntity> userPage = userRepository.findAll(pageableRequest);
		Page<UserEntity> userPage;
		if(search.isEmpty()) {
			userPage = userRepository.getAllUser(pageableRequest);
		}else {
			userPage = userRepository.findAllUserByCriteria(pageableRequest, search, status);
		}
		
		List<UserEntity> users = userPage.getContent();//la methode .getContent() contient la liste des utilisateurs dans la page récupéré
		
		for(UserEntity userEntity: users) {
			//UserDto userD = new UserDto();
			//BeanUtils.copyProperties(userEntity, userD);
			
			ModelMapper modelMapper = new ModelMapper();
			UserDto userD = modelMapper.map(userEntity, UserDto.class);
			
			usersDto.add(userD);
		}
		
		return usersDto;
	}

}
