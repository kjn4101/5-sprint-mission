package com.codeit.mvc.controller;


import com.codeit.mvc.domain.User;
import com.codeit.mvc.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * ■ Controller 주요 개념 정리
 *
 * 1. 핸들러 메서드의 URL 맵핑 관련 어노테이션 정리
 *
 *  1) @RequestMapping
 *   - 개념 : 가장 기본적인 Spring의 URL 매핑 어노테이션, GET/POST 둘 다 처리 가능함
 *   - 속성 값이 없을 때 : value는 URL을 나타내고, method를 지정하지 않으면 GET/POST 둘 다 처리함
 *   - value : 매핑할 URL을 지정하는 속성, '/' 생략 가능, 여러 개의 URL 지정 가능
 *   - method : GET, POST 등 HTTP 메서드 지정. 지정하지 않으면 모든 메서드 처리
 *   - 예시:
 *     @RequestMapping("home.do")
 *     @RequestMapping(value = "home.do")
 *     @RequestMapping(value = "home.do", method = RequestMethod.GET)
 *     @RequestMapping(value = {"home.do", "index.do"}, method = {RequestMethod.GET, RequestMethod.POST})
 *
 *  2) @GetMapping (Spring 4.3+)
 *   - GET 방식 전용 매핑 어노테이션
 *   - 예시: @GetMapping("/getHome.do")
 *
 *  3) @PostMapping
 *   - POST 방식 전용 매핑 어노테이션
 *   - 예시: @PostMapping("/postHome.do")
 *
 *  4) @PutMapping, @DeleteMapping 등도 존재함
 *   - 각각 PUT, DELETE HTTP 요청을 처리함
 *
 *
 * 2. 핸들러 메서드 주요 파라메터(매개 변수, Parameter, 인자) 정리
 *  - 스프링 MVC는 다양한 타입의 파라메터를 자동으로 주입(injection)해줌
 *   1) HttpServletRequest : 사용자의 요청 정보 (javax.servlet), 서블릿 API와 동일
 *   2) HttpServletResponse : 사용자에게 응답 보낼 객체 (javax.servlet), 서블릿 API와 동일
 *   3) HttpSession : 브라우저 연결 시점부터 종료 시점까지 데이터를 저장하는 세션 객체 ★★★★★
 *   4) Locale : 요청자의 지역 정보 (언어, 국가)
 *   5) OutputStream / InputStream : 바이너리 파일, JSON 전송 시 사용
 *   6) Reader / Writer : 문자 기반 스트림 처리용
 *   7) Model : JSP에 데이터를 넘기는 데 사용, Request 객체보다 편리함 ★★★★★
 *   8) ModelAndView : 데이터(Model) + View 이름을 함께 저장하는 객체 ★★★★★
 *   9) Map<String, Object> : 요청 파라미터를 Map 형태로 자동 바인딩 ★★★★★
 *  10) 커맨드 객체(Command Object, VO/DTO) ★★★★★
 *     - 요청 파라미터를 VO 객체의 필드에 자동 바인딩해줌
 *     - JavaBean 규약(기본 생성자, getter/setter)을 따라야 함
 *     - 필드명은 form name과 동일해야 자동 매핑됨
 *     - 복잡한 폼 데이터를 깔끔하게 처리할 수 있어 권장됨
 *  11) 일반 파라메터 ★★★★★
 *     - @RequestParam을 이용해 name 속성과 메서드 파라메터를 바인딩함
 *     - 예: @RequestParam("id") String id
 *     - 옵션: defaultValue = 기본값 설정, required = 필수 여부 지정
 *
 * 3. 핸들러 메서드 파라메터 관련 어노테이션 정리
 *  ■ 일반 Web 관련 어노테이션
 *  - @RequestParam(value="parameter이름", 옵션...) String id
 *     : form의 name 값과 파라메터를 매핑함
 *     : 옵션 → defaultValue: 값이 없을 경우 기본값, required: 필수 여부 설정
 *  - @RequestHeader(value="헤더이름") String ip
 *     : HTTP 헤더 값을 읽을 때 사용
 *  - @ModelAttribute("model명")
 *     : View에 넘길 데이터를 지정된 이름으로 바인딩하거나, 커맨드 객체 바인딩에 사용
 *  - @CookieValue(value="쿠키이름") String cookie
 *     : 요청에 포함된 쿠키 값을 가져옴
 *     : required = false 시 쿠키가 없어도 예외 없이 null 주입됨
 *  - @SessionAttribute("세션이름")
 *     : 세션에서 값을 읽어와 파라미터로 바인딩함
 *     : required = false 옵션으로 null 허용 가능
 */

@Controller
// class 레벨에서 /users 경로를 붙여주는 역할
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    
    // @RequestMapping : 가장 기본적인 요청 처리 어노테이션, 최초의 url 맵핑 어노테이션이며 기능이 많다.
    // /index.do <- 레거시한 표현으로 UserController로 요청하여 동적페이지는 의미하는 확장자
    @RequestMapping(value = {"/index.do", "/index", "/"},
                    method = {RequestMethod.GET, RequestMethod.POST})
    public String index(/*파라메터 주입 자리*/){ // <- 핸들러 메서드
        return "users/index"; // view의 이름을 리턴
    }

    // ----------------------- 핸들러 메서드 스타일 정리 -------------------------------- //

    // 1. 서블릿 스타일
    // - 장점 : 서블릿 프로젝트 호환용, 종종 쓴다.
    // - 단점 : 복잡하다. 안쓴다.

    // get 처리
    @GetMapping("/userServlet")
    public String userServletPage(){
        return "users/userEnrollForServlet";
    }

    // post 처리
    @PostMapping("/userServlet")
    public String userServlet(HttpServletRequest request, HttpServletResponse response,
                                                                      HttpSession session){
        User user = new User();
        user.setId(Long.parseLong(request.getParameter("id")));
        user.setUsername(request.getParameter("username"));
        user.setPassword(request.getParameter("password"));
        user.setName(request.getParameter("name"));
        user.setRoles(request.getParameter("roles"));
        user.setDevLang(Arrays.asList(request.getParameterValues("devLang")));

        // model 대신하는 방법
        request.setAttribute("user", user);
        
        // 쿠키 활용법
        Cookie cookie = new Cookie("saveUsername", user.getUsername());
        cookie.setMaxAge(60 * 60); // 1시간
        response.addCookie(cookie);

        // 세션 가져오는 방법1
        request.getSession().setAttribute("username", user.getUsername());

        // 세션 가져오는 방법 2, spring에서 세션 사용 하는 방법중 하나
        session.setAttribute("username", user.getUsername());

        return "users/userView";
    }

    // 2. @RequestParam을 통해 View - Form의 파라미터를 받아오는 방법
    // - RequestParam을 통해 form에 있는 name과 핸들러 메서드의 파라미터를 맵핑시키는 방법
    // - 파라미터가 1~2개 까지는 해당 방법을 권장, 3개 이상 넘어가면 DTO 활용 권장
    // - model : view로 데이터를 보낼때 사용하는 객체
    // - @RequestParam(value="form의 name") : 파라미터를 받아오는 방법
    //    -> 만일 form name과 메소드의 파라미터 인자명이 일치하면 생략 가능, 만일 다르면 반드시 있어야한다.
    // - 옵션 : defaultValue = 만일 값이 없으면 기본값 셋팅, required = 필수값 셋팅, 없으면 처리X
    // - 만일 배열의 파라미터인 경우, List<String>, String[] 둘다 처리가능
    @GetMapping("/userParams")
    public String userParamsPage(){
        return "users/userEnrollForParams";
    }

    @PostMapping("/userParams")
    public String userParams1(Model model,
                              @RequestParam Long id,
                              @RequestParam String username,
                              @RequestParam(value = "pwd") String password,
                              @RequestParam String name,
                              @RequestParam String roles,
                              @RequestParam(value = "devLang", required = false) List<String> devLang
                              ){
        System.out.println(devLang); // 없으면 null
        User user = new User(id, username, password, name, roles, devLang);
        model.addAttribute("user", user);

        return "users/userView";
    }

    // @RequestParam 생략한 버전 -> 문법적으로는 가능!
    // 가능하면 @RequestParam 어노테이션을 명시적으로 사용하는 것을 권장함 (레거시에는 잘 안썼다.)
    // 주의점 : List의 경우 @RequestParam 을 생략해서 사용할수 없다!
    @PostMapping("/userParams2")
    public String userParams2(Model model, Long id, String username, String pwd,
                                    String name, String roles, String[] devLang){
        User user = new User(id, username, pwd, name, roles, Arrays.asList(devLang));
        model.addAttribute("user", user);
        return "users/userView";
    }


    // 3. Command 객체(VO, DTO)로 파라미터 처리하는 방법
    // - 자바의 객체와 From의 name을 일치시켜 파라미터를 바인딩하는 방식
    // - 반드시 변수 이름과 type 일치해야 자동으로 바인딩 된다.
    // - ※ 주의 : 바인딩 될 객체에 기본 자료형(문자열), 배열, List만 허용 가능, 이외 객체형은 처리 안됨, ex) Date, LocalDate
    // - 복잡한 타입은 Converter 또는 Formatter가 필요 -> 날짜는 Formatter로 쉽게 처리가능하다.
    //   ex) @DateTimeFormat(pattern = "yyyy-MM-dd") private LocalDate birthDate;
    // - 실제 현업에서 가장 많이 활용되는 스타일로 반드시 알고있자.

    // @ModelAttribute : View에 넘길 데이터를 지정된 이름으로 바인딩하거나, 커맨드 객체 바인딩에 사용, 약간 관례적!, Rest에서는 빼도 무관
    @GetMapping("/userCommand")
    public String userCommandPage() {
        return "users/userEnrollForCommand";
    }

    @PostMapping("/userCommand2")
    public String userCommand1(Model model, User user){
        model.addAttribute("user", user);
        return "users/userView";
    }

    // Model 없이 View로 전달하는 방법 : @ModelAttribute
    @PostMapping("/userCommand")
    public String userCommand2(@ModelAttribute User user){
        return "users/userView";
    }


    // 4. 컬렉션의 Map 활용법
    // - form 파라미터의 객체를 map 으로 가져오는 방법 (@RequestParam 필수!)
    // - 장점 : 객체 상관 없이 모든 파라미터를 map으로 담아 편하게 처리할수 있다.
    // - 단점 : 맵으로 담기 때문에 Type 안정성이 떨어지고, 유지보수는 떨어진다.
    // - 현업에서는 map 인자를 받아서 DB나 Mybatis로 넘겨 동적쿼리로만 인자를 활용하는 패턴
    // - 해당 방법을 권장하지는 않는다. 알고만 있자!
    @GetMapping("/userMap")
    public String userMapPage() {
        return "users/userEnrollForMap";
    }


    // list류는 맵으로 처리 불가!
    @PostMapping("/userMap2")
    public String userMap1(Model model, @RequestParam Map<String, Object> map){
        model.addAttribute("user", map);
        return "users/userView";
    }

    // list를 따로 받는 코드 추가한 버전!
    @PostMapping("/userMap")
    public String userMap2(Model model, @RequestParam Map<String, Object> map,
                            @RequestParam(value = "devLang", required = false) List<String> devLang  ){
        map.put("devLang", devLang);
        model.addAttribute("user", map);
        return "users/userView";
    }

    // 5. header, cookie, Session 정보 받아 오기
    // Writer : 사용자가 직접 본문을 작성할때 사용할수 있다. -> response에 있었던 객체
    // @RequestHeader : header값 가져오는 어노테이션
    // @CookieValue : 쿠기 정보를 가져올때 사용하는 어노테이션, required = false 일때 null 허용된다.
    // @SessionAttribute : 세션 정보를 가져올때 사용하는 어노테이션, required = false 일때 null 허용된다.
    // Locale : 사용자 요청 언어
    // void인 경우는 return 없을때 활용 -> view가 존재하지 않는다!
    // 참고로 REST는 더 좋게 만드는 방법이 있음으로 Writer 쓰지 말것.
    @GetMapping("/userAddInfo")
    public void userAddInfo(Writer writer,
                              @RequestHeader(value = "User-Agent") String userAgent,
                              @RequestHeader(value = "Accept", required = false) String accept,
                              @RequestHeader(value = "Content-Type", required = false) String ContentType,
                              @RequestHeader(value = "referer", required = false) String referer,
                              @CookieValue(value = "saveUsername", required = false) String cookieUsername,
                              @SessionAttribute(value = "username", required = false) String sessionUsername,
                              Locale locale
                              ) throws IOException {
        writer.append("<html>");
        writer.append("userAgent : " + userAgent + "<br>");
        writer.append("accept : " + accept + "<br>");
        writer.append("ContentType : " + ContentType + "<br>");
        writer.append("referer : " + referer + "<br>");
        writer.append("cookieUsername : " + cookieUsername + "<br>");
        writer.append("sessionUsername : " + sessionUsername + "<br>");
        writer.append("locale : " + locale + "<br>");
        writer.append("</html>");
    }

    @GetMapping("/userAddInfo2")
    @ResponseBody // view 없이 사용자가 직접 응답값을 작성할때 사용
    public String userAddInfo(
                              @RequestHeader(value = "User-Agent") String userAgent,
                              @RequestHeader(value = "Accept", required = false) String accept,
                              @RequestHeader(value = "Content-Type", required = false) String ContentType,
                              @RequestHeader(value = "referer", required = false) String referer,
                              @CookieValue(value = "saveUsername", required = false) String cookieUsername,
                              @SessionAttribute(value = "username", required = false) String sessionUsername
                              ) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("userAgent : " + userAgent + "<br>");
        sb.append("accept : " + accept + "<br>");
        sb.append("ContentType : " + ContentType + "<br>");
        sb.append("referer : " + referer + "<br>");
        sb.append("cookieUsername : " + cookieUsername + "<br>");
        sb.append("sessionUsername : " + sessionUsername + "<br>");
        sb.append("</html>");
        return sb.toString();
    }

    // ModelAndView 패턴
    // - Spring에서 올드한 패턴중 하나로 필드에서 가장 빈번되는 활용되는 스타일 (=전자정부 프레임워크)
    // - addObject : view로 보낼 파라미터를 처리하는 방법 model의 일
    // - setViewName : return 하였던 view의 이름을 설정하는 메소드
    @RequestMapping(value = "/userEnroll", method = RequestMethod.GET)
    public ModelAndView userEnrollPage(ModelAndView modelAndView){
        modelAndView.setViewName("users/userEnroll");
        return modelAndView;
    }

    @RequestMapping(value = "/userEnroll", method = RequestMethod.POST)
    public ModelAndView userEnroll(ModelAndView modelAndView, User user){
        User result = userService.register(user);
        if(result != null && user.getId() != 100L){ // 회원가입이 성공적일때
            modelAndView.addObject("msg", "회원가입에 성공하였습니다!");
            modelAndView.addObject("location", "/users/index.do");
            modelAndView.setViewName("common/msg");
        } else{
            modelAndView.setViewName("redirect:/users/error.do"); // 핸들러 끼리 연결시키는 방법중 하나!
//            modelAndView.setViewName("common/error"); // redirect이 아닌 에러 페이지를 뷰로 만드는 방법!
        }
        return modelAndView;
    }

    // 7. List를 통한 회원정보 보기
    @GetMapping("/userList")
    public String userList(Model model){
        List<User> list = userService.getAllUsers();
        model.addAttribute("list", list);
        return "users/userList";
    }

    // 8. 에러 페이지 (redirect 실험용)
    @GetMapping("/error.do")
    public String error(Model model) {
        return "common/error";
    }

}

