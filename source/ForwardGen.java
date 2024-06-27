package CemuFW;
import javax.xml.parsers.*;
import java.awt.image.*;
import javax.swing.*;
import javax.imageio.*;
import java.awt.Color;
import org.w3c.dom.*;
import java.io.*;
import net.npe.tga.TGAReader;
public class ForwardGen{
    public static void createLauncher(String path, Boolean fullscreen, Boolean debug, String extraOptions){
        File file = new File(path+"/meta/meta.xml");
        String name = "uknownGame";
        try{
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document titleXML = db.parse(file);
            titleXML.getDocumentElement().normalize();
            name = titleXML.getElementsByTagName("shortname_en").item(0).getTextContent();
        }catch(Exception e){
            System.out.println("Error: failed to open and read meta.xml [target: Uknown]");
        }
        String desktopName = name.toLowerCase().replaceAll(" ","-");
        System.out.println("Identified: "+name);
        if(System.getProperty("os.name").toUpperCase().contains("WINDOWS")){
            System.out.println("Error: No windows implementation yet ):\ncomming soon...\n[target: Windows]");
        }else{
            try{
                File userHome = new File(System.getProperty("user.home")+"/.CemuForwarder/"+desktopName);
                userHome.mkdirs();
                FileInputStream fileTga = new FileInputStream(path+"/meta/iconTex.tga");
                byte [] buffer = new byte[fileTga.available()];
                fileTga.read(buffer);
                fileTga.close();
                int [] pixels = TGAReader.read(buffer, TGAReader.ARGB);
                BufferedImage loadedIcon = new BufferedImage(
                    TGAReader.getWidth(buffer),
                    TGAReader.getHeight(buffer),
                    BufferedImage.TYPE_INT_ARGB
                );
                loadedIcon.setRGB(
                    0,
                    0,
                    TGAReader.getWidth(buffer),
                    TGAReader.getHeight(buffer),
                    pixels,
                    0,
                    TGAReader.getWidth(buffer)
                );
                BufferedImage jpegIcon = new BufferedImage(loadedIcon.getWidth(),loadedIcon.getHeight(),BufferedImage.TYPE_INT_RGB);
                jpegIcon.createGraphics().drawImage(loadedIcon,0,0,Color.WHITE,null);
                FileOutputStream exportImage = new FileOutputStream(System.getProperty("user.home")+"/.CemuForwarder/"+desktopName+"/iconTex.jpg");
                ImageIO.write(jpegIcon,"jpg",exportImage);
                exportImage.close();
                System.out.println("Converted iconTex.tga -> iconTex.jpg");
                FileWriter desktopForward = new FileWriter(System.getProperty("user.home")+"/.local/share/applications/"+desktopName+".desktop");
                String launcher = "cemu -g \""+path+"\" "+extraOptions+" ";
                if(fullscreen){
                    launcher += "-f";
                }
                if(debug){
                    launcher = "sh -c '"+launcher+";$SHELL'\nTerminal=true";
                }
                desktopForward.write(
                    "[Desktop Entry]\n"+
                    "Type=Application\n"+
                    "Name="+name+"\n"+
                    "Comment=Play in Cemu\n"+
                    "Exec="+launcher+"\n"+
                    "Icon="+System.getProperty("user.home")+"/.CemuForwarder/"+desktopName+"/iconTex.jpg\n"+
                    "Catagories=Game;\n"
                );
                desktopForward.close();
                JOptionPane.showMessageDialog(null,"It may take a while for your desktop to find the shortcuts. (Rebooting may help.)","Forward Complete!",JOptionPane.INFORMATION_MESSAGE);
            }catch(IOException e){
                System.out.println("Error: Could not create shortcut \n[target: Linux]");
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}