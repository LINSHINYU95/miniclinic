package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StatsController {

    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final AppointmentRepository appointmentRepo;

    public StatsController(DoctorRepository doctorRepo, 
                           PatientRepository patientRepo, 
                           AppointmentRepository appointmentRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.appointmentRepo = appointmentRepo;
    }

    @GetMapping("/stats")
    public String showStatistics(Model model) {
        model.addAttribute("doctorCount", doctorRepo.count());
        model.addAttribute("patientCount", patientRepo.count());
        model.addAttribute("appointmentCount", appointmentRepo.count());
        
        // 取得依科別分組的統計資料，並轉換為 Map 方便前端讀取
        List<Object[]> results = appointmentRepo.countAppointmentsByDepartment();
        Map<String, Long> statsByDept = results.stream()
            .collect(Collectors.toMap(res -> (String) res[0], res -> (Long) res[1]));
        model.addAttribute("statsByDept", statsByDept);
        
        return "stats"; // 導向到 stats.html 頁面
    }
}