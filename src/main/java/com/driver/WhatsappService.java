package com.driver;

import com.driver.Exceptions.approversIsNotAdmin;
import com.driver.Exceptions.groupDoesNotExist;
import com.driver.Exceptions.userAlreadyExists;
import com.driver.Exceptions.userNotExist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service

public class WhatsappService {

    WhatsappRepository whatsappRepositoryObj=new WhatsappRepository();

    public WhatsappService() {

    }

    public void createUser(String name, String mobile) throws Exception {
           if(whatsappRepositoryObj.getUserMobile().contains(mobile))
               throw new userAlreadyExists("User already exists");
           User user=new User(name,mobile);
           whatsappRepositoryObj.getUserMobile().add(mobile);


    }
    public Group createGroup(List<User> users){

        if(users.size()<2)
            return null;
        for(User user:users){
            if(whatsappRepositoryObj.getUserMobile().equals(user.getMobile())||whatsappRepositoryObj.getNames().equals(user.getName()))
                users.remove(user);
            whatsappRepositoryObj.getUserMobile().add(user.getMobile());
            whatsappRepositoryObj.getNames().add(user.getName());
        }
        if(users.size()==2){
            String name=users.get(users.size()-1).getName();
            Group group=new Group(name, users.size());
            whatsappRepositoryObj.getAdminMap().put(group,users.get(0));
            whatsappRepositoryObj.getGroupUserMap().put(group,users);
            return group;
        }
        else{
            whatsappRepositoryObj.setCustomGroupCount(whatsappRepositoryObj.getCustomGroupCount()+1);
            String name="Group "+whatsappRepositoryObj.getCustomGroupCount();

            Group group=new Group(name, users.size());
            whatsappRepositoryObj.getAdminMap().put(group,users.get(0));
            whatsappRepositoryObj.getGroupUserMap().put(group,users);
            return group;
        }
    }
 public int createMessage(String content){
        whatsappRepositoryObj.setMessageId(whatsappRepositoryObj.getMessageId()+1);
        return whatsappRepositoryObj.getMessageId();
 }
 public int sendMessage(Message message, User sender, Group group)throws Exception{
      if(!whatsappRepositoryObj.getGroupUserMap().containsKey(group))
          throw new groupDoesNotExist("Group does not exist");
      Boolean find=false;
      for(User user:whatsappRepositoryObj.getGroupUserMap().get(group)){
          if(user.equals(sender)){
              find=true;
              break;
          }
      }
      if(!find)
          throw new userNotExist("You are not allowed to send message");
      whatsappRepositoryObj.getSenderMap().put(message,sender);
      if(whatsappRepositoryObj.getGroupMessageMap().containsKey(group)){
          whatsappRepositoryObj.getGroupMessageMap().get(group).add(message);
          return whatsappRepositoryObj.getGroupMessageMap().get(group).size();
      }
      List<Message>msg= new ArrayList<>();
      msg.add(message);

      whatsappRepositoryObj.getGroupMessageMap().put(group,msg);
      return 1;
 }
 public String changeAdmin(User approver, User newAdmin, Group group)throws Exception{
     if(!whatsappRepositoryObj.getGroupUserMap().containsKey(group))
         throw new groupDoesNotExist("Group does not exist");
     if(!whatsappRepositoryObj.getAdminMap().get(group).getName().equals(approver))
         throw new approversIsNotAdmin("Approver does not have rights");
     Boolean find=false;
     for(User user:whatsappRepositoryObj.getGroupUserMap().get(group)){
         if(user.equals(newAdmin)){
             find=true;
             break;
         }
     }
     if(!find)
         throw new userNotExist("User is not a participant");

     whatsappRepositoryObj.getAdminMap().put(group,newAdmin);


    return "SUCCESS";


 }
 public int removeUser(User user)throws Exception{
        return 0;
 }
 public String findMessage(Date start, Date end, int K) throws Exception{
        return null;
 }

}
