package tw.edu.fju.miniclinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import tw.edu.fju.miniclinic.model.Appointment;
import tw.edu.fju.miniclinic.model.AppointmentRepository;
import tw.edu.fju.miniclinic.model.DoctorRepository;
import tw.edu.fju.miniclinic.model.PatientRepository;

import java.util.HashMap;
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

    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getApiStats() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalDoctors", doctorRepo.count());
        response.put("totalPatients", patientRepo.count());
        
        List<Appointment> allAppts = appointmentRepo.findAll();
        response.put("totalAppointments", (long) allAppts.size());

        // 依照掛號狀態進行分組計數
        Map<String, Long> byStatus = allAppts.stream()
            .collect(Collectors.groupingBy(Appointment::getStatus, Collectors.counting()));
        
        // 確保回傳的 JSON 包含所有必要欄位，若無資料則補 0
        byStatus.putIfAbsent("BOOKED", 0L);
        byStatus.putIfAbsent("COMPLETED", 0L);
        byStatus.putIfAbsent("CANCELLED", 0L);
        
        response.put("byStatus", byStatus);
        
        return response;
    }
}