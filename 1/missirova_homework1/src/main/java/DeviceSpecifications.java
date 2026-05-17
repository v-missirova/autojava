import java.io.*;
import java.util.List;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.FormatUtil;


@WebServlet(name = "deviceSpecs", value = "/device-specs")
public class DeviceSpecifications extends HttpServlet {
    private String message;

    public void init() {
        message = "device specifications";

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        SystemInfo systemInfo = new SystemInfo();

        HardwareAbstractionLayer hardware = systemInfo.getHardware();
        CentralProcessor processor = hardware.getProcessor();
        CentralProcessor.ProcessorIdentifier processorIdentifier = processor.getProcessorIdentifier();

        GlobalMemory globalMemory = hardware.getMemory();

        List<GraphicsCard> graphicsCards = systemInfo.getHardware().getGraphicsCards();



        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<p><b>CPU name:</b> " + processorIdentifier.getName() + "</p><hr>");

        out.println("<p><b>Total memory:</b> " + FormatUtil.formatBytes(globalMemory.getTotal()) + "</p>");
        out.println("<p><b>Available memory:</b> " + FormatUtil.formatBytes(globalMemory.getAvailable()) + "</p><hr>");

        for (GraphicsCard gpu : graphicsCards) {
            out.println("<p><b>GPU name:</b> " + gpu.getName() + "; <b>VRAM:</b> " + FormatUtil.formatBytes(gpu.getVRam())+"</p>");
        }
        out.println("</body></html>");
    }

    public void destroy() {
    }
}