package com.adminportal.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity
public class Usercreditcard {
	   @Id
	    private Long id;
	    private String type;
	    private String holdername;
	    private boolean defaul;
	    @ManyToOne
		@JoinColumn(name="user_id")
	    private User user;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getHoldername() {
			return holdername;
		}
		public void setHoldername(String holdername) {
			this.holdername = holdername;
		}
		public boolean isDefaul() {
			return defaul;
		}
		public void setDefaul(boolean defaul) {
			this.defaul = defaul;
		}
		public User getUser() {
			return user;
		}
		public void setUser(User user) {
			this.user = user;
		}
	    
}
