package com.sheasepherd.nescue.controller;
import com.sheasepherd.nescue.NescueService;
import com.sheasepherd.nescue.model.Size;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;


@WebMvcTest(ReportController.class)
public class ReportControllerTest {

    @Autowired
    private MockMvcTester mockMvc;

    @MockitoBean
    private NescueService nescueService;

    @Test
    void anonymReportIsSaved() {
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
        ).hasStatus3xxRedirection()
                .hasRedirectedUrl("/report?success");
        verify(nescueService).reportNet(isNull(), isNull(), isNull(), eq(Size.LARGE), any());
    }

    @Test
    void anonymReportMissingSize() {
        assertThat(mockMvc.post().uri("/report")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("size");
        verifyNoInteractions(nescueService);
    }

    @Test
    void anonymReportMissingLatitude(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("longitude", "127.24513")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("latitude");
        verifyNoInteractions(nescueService);
    }

    @Test
    @Disabled("Wertebereiche werden durch einen Formatter gelöst")
    void anonymReportLatitudeValueOutOfLowBoundaries() {
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "-92")
                .param("longitude", "127.24513")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("latitude");
        verifyNoInteractions(nescueService);
    }

    @Test
    @Disabled("Wertebereiche werden durch einen Formatter gelöst")
    void anonymReportLatitudeValueOutOfHighBoundaries(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "92")
                .param("longitude", "127.24513")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("latitude");
        verifyNoInteractions(nescueService);
    }

    @Test
    void anonymReportMissingLongitude() {
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("longitude");
        verifyNoInteractions(nescueService);
    }

    @Test
    @Disabled("Wertebereiche werden durch einen Formatter gelöst")
    void anonymReportLongitudeValueOutOfLowBoundaries(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "-182")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("longitude");
        verifyNoInteractions(nescueService);
    }

    @Test
    @Disabled("Wertebereiche werden durch einen Formatter gelöst")
    void anonymReportLongitudeValueOutOfHighBoundaries() {
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "182")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("latitude");
        verifyNoInteractions(nescueService);
    }

    @Test
    void knownReportIsSaved(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber", "+12724513124")
        ).hasStatus3xxRedirection().hasRedirectedUrl("/report?success");
        verify(nescueService).reportNet(eq("Clara"), eq("Testfall"), eq("+12724513124"), eq(Size.LARGE), any());
    }

    @Test
    void knownReportMissingFirstname(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
                .param("lastname", "Testfall")
                .param("phoneNumber", "+12724513124")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("completeContactDataInForm");
        verifyNoInteractions(nescueService);
    }

    @Test
    void knownReportMissingLastname(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
                .param("firstname", "Clara")
                .param("phoneNumber", "+12724513124")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("completeContactDataInForm");
        verifyNoInteractions(nescueService);
    }
    @Test
    void knownReportMissingPhoneNumber(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("completeContactDataInForm");
        verifyNoInteractions(nescueService);
    }
    @Test
    void knownReportPhoneNumberWithoutPlus(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber", "12724513124")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("phoneNumber");
        verifyNoInteractions(nescueService);
    }
    @Test
    void knownReportPhoneNumberWithLetters(){
        assertThat(mockMvc.post().uri("/report")
                .param("size", "LARGE")
                .param("latitude", "23.12523")
                .param("longitude", "127.24513")
                .param("firstname", "Clara")
                .param("lastname", "Testfall")
                .param("phoneNumber", "+127A45B3ADV")
        ).model().extractingBindingResult("reportForm").hasFieldErrors("phoneNumber");
        verifyNoInteractions(nescueService);
    }
}