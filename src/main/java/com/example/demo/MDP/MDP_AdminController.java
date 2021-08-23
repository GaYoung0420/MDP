package com.example.demo.MDP;

import org.springframework.ui.Model;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.example.demo.MDP.MDP_Security_DTO.SecurityAdmins;
import com.example.demo.MDP.MDP_Security_DTO.SecurityRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("MDPadmin")
public class MDP_AdminController {

    @Autowired
    private mdpRepository mdpRepo;
    
    @Autowired
    private PasswordEncoder pwEncoder;

    @Autowired
    private saRepository saRepo;

    @Autowired
    private checkID checkID;
    
    @GetMapping("/login")
    public String adminLogin(){
        return "admin/login";
    }

    @GetMapping("/join")
    public String admin_join(){
        return "admin/join";
    }

    @PostMapping("/join")
    public String admin_join(SecurityAdmins sa, String code) {

        //승인코드 검사
        if((code.equals("mdp2021"))==false){
            return "redirect:join";
        }

        //비밀번호 암호화
        String encodedpw = pwEncoder.encode(sa.getPassword());
        sa.setPassword(encodedpw);
        sa.setEnabled(true);

        SecurityRole sr = new SecurityRole();
        if(checkID.sacheckJoin(sa.getUsername())){
            sr.setId(1l);
            sa.getRoles().add(sr);
            saRepo.save(sa);
            return "redirect:login";
        }
        else{
            return "redirect:join";
        }
    }

    @GetMapping("/manage")
    public String manage(Model model, @RequestParam(required = false, defaultValue = "") String searchText, @PageableDefault(size = 15) Pageable pageable){
        //페이징
        Page<mdpPurchaseCode> page = mdpRepo.findAll(pageable);

        int startPage;
        int endPage;

        //한 번에 뜨는 페이지의 개수가 5개 혹은 그 미만으로 뜨게 하기 위한 조건문
        //현재 페이지가 1,2인 경우 총 페이지가 5미만일 때는 마지막 페이지까지, 총 페이지가 5 이상일 때는 5까지 출력
        if(page.getPageable().getPageNumber()<2){
            startPage=1;
            if(page.getTotalPages()<5){
                endPage=page.getTotalPages();
            }
            else
                endPage=5;
        }
        //현재 페이지가 3이상인 경우 첫페이지는 현재페이지-2, 마지막 페이지는 총 페이지의 개수 혹은 현재 페이지+3 중 작은 값
        else{
            startPage=page.getPageable().getPageNumber() - 1;
            endPage=Math.min(page.getTotalPages(), page.getPageable().getPageNumber() + 3);
        }

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("total", page.getTotalPages()); //총 페이지 수
        model.addAttribute("pages", page.getPageable().getPageNumber()); // 현재 페이지
        model.addAttribute("list", page);

        return "admin/manage";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(required = false, defaultValue = "") String searchText, @PageableDefault(size = 15) Pageable pageable){
        
        //아이디 검색
        Page<mdpPurchaseCode> page = mdpRepo.findByUser(searchText,pageable);
        int startPage = Math.max(1, page.getPageable().getPageNumber() - 9);
        int endPage = Math.min(page.getTotalPages(), page.getPageable().getPageNumber() + 9);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("list", page);
        
        return "admin/manage";
    }

    @GetMapping("/add")
    public String add(Model model, @RequestParam int count){
        mdpPurchaseCode mp = new mdpPurchaseCode();

        Random random = new Random();
        long last=mdpRepo.lastColumn();

        for(int i=0; i<count; i++){
            String pc = "THINKUS_";
            mp.setId(Long.valueOf(i+last+1l)); //id 설정
            for(int j=0; j<5; j++){
                int rd=random.nextInt(2);
                char ch=(char)((Math.random()*26)+65);
                if(rd==1){
                    pc=pc.concat(Integer.toString(random.nextInt(10)));
                }
                else{
                    pc=pc.concat(Character.toString(ch));
                }
            }
            mp.setCode(pc);
            
            //중복 제거
            if(mdpRepo.countByCode(mp.getCode())>0){
                i--;
                continue;
            }
            
            mdpRepo.save(mp);
            mdpRepo.flush();
        }

        return "redirect:manage";
        
    }

    @GetMapping("/adminLogout")
    public String adminLogout(HttpServletRequest request, Model model){
        HttpSession session = request.getSession();
        session.invalidate();
        return "admin/login";        
    }
}
