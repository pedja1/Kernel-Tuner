package rs.pedjaapps.kerneltuner.filemanager;

import android.text.TextUtils;

import org.apache.commons.io.FilenameUtils;

import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

/**
 * Created by pedja on 10.8.14..
 */
public class FMUtils
{
    public enum FileType
    {
        unknown, image, text, video, application, database, archive, web/*html, php...*/, audio, doc, pdf, ppt,
        excel, lib, script, code/*formatted text (eg. xml, java, json...)*/;

        String mValue;

        FileType(String mValue)
        {
            this.mValue = mValue;
        }

        FileType()
        {
        }

        @Override
        public String toString()
        {
            return mValue == null ? super.toString() : mValue;
        }
    }

    public static String getMimeTypeForFile(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        return fileNameMap.getContentTypeFor(path);
    }

    public static FileType getFileType(String path)
    {
        if(!TextUtils.isEmpty(path))
        {
            /*application;*/
            String ext = FilenameUtils.getExtension(path);
            if("apk".equals(ext))return FileType.application;
            if("pdf".equals(ext))return FileType.pdf;
            if("ppt".equals(ext))return FileType.ppt;
            if("xls".equals(ext))return FileType.excel;
            if("script".equals(ext))return FileType.script;
            else if(isImage(ext))return FileType.image;
            else if(isText(ext))return FileType.text;
            else if(isVideo(ext))return FileType.video;
            else if(isDb(ext))return FileType.database;
            else if(isArchive(ext))return FileType.archive;
            else if(isWeb(ext))return FileType.web;
            else if (isAudio(ext))return FileType.audio;
            else if (isDoc(ext))return FileType.doc;
            else if(isLib(ext))return FileType.lib;
            else if(isCode(ext))return FileType.code;
        }
        return FileType.unknown;
    }

    private static boolean isCode(String ext)
    {
        List<String> imageExts = Arrays.asList(("xml,php,php3,php4,php5,css,js,c,class,cpp,cs,dtd,fla,h," +
                "java,lua,m,pl,py,sh,sln,swift,vcxproj,xcodeproj,as,as3proj,asc,bbprojectd,cp,csproj," +
                "dcproj,dex,dpr,dproj,erb,exp,fs,fsproj,fsx,ftl,gem,gfar,gmk,groupproj,gs,hpp,ise,jspf," +
                "m,markdown,md,mm,mshc,nib,ocx,pas,pas,pbj,pbxproj,pbxuser,pch,playground,pod,pro,proto," +
                "psm1,r,rb,rbw,res,resx,sdef,src,trx,v,vbproj,vcproj,vdproj,vtm,wixproj,xq,xsd,yml,4db," +
                "4th,a,a2w,abc,acd,addin,ads,agi,alb,am4,am5,am6,am7,ane,apa,appx,appxupload,aps,ap_," +
                "arsc,artproj,as2proj,asi,asm,asm,asvf,au3,autoplay,awk,b,bas,bb,bbc,bbproject,bcp," +
                "bdsproj,bet,bluej,bpg,bpl,brx,bs2,bsc,c,caf,caproj,capx,cbl,cbp,cc,ccgame,ccn,ccp," +
                "ccs,cd,cdf,cfc,clips,cls,clw,cob,cod,config,cp,csi,csi,csn,csp,csx,ctl,ctp,ctxt,cu," +
                "cvsrc,cxp,cxx,d,dba,dba,dbml,dbo,dbpro,dbproj,dcp,dcu,dcuil,dec,def,deviceids,df1," +
                "dfm,dgml,dgsl,diff,dm1,dmd,dob,dox,dpk,dpkw,dpl,dsgm,dsp,edml,edmx,ent,entitlements," +
                "eql,erl,ex,exw,f,f90,fbp,fbz7,fgl,for,forth,fpm,framework,frx,fsi,fsproj,fsscript," +
                "ftn,fxc,fxcproj,fxl,fxml,fxpl,gameproj,gch,ged,gemspec,gitattributes,gitignore,gld," +
                "gm6,gm81,gmd,gmo,gmx,gorm,greenfoot,groovy,gs3,gsproj,gszip,hal,haml,has,hbs,hh,hpf," +
                "hs,hxx,i,idb,idl,idt,ilk,iml,inc,inl,ino,ipch,ipr,ipr,ism,ist,iwb,iws,jcp,jic,jpr,jpx," +
                "jsfl,kdevelop,kdevprj,kpl,l,lbi,lbs,lds,lgo,lhs,licenses,licx,lisp,lit,livecode,lnt," +
                "lproj,lsproj,ltb,lucidsnippet,lxsproj,m4,magik,mak,mcp,mdzip,mer,mf,mfa,mk,ml,mo,mod," +
                "mom,mpr,mrt,msha,mshi,msl,msp,mss,mv,mxml,myapp,nbc,ncb,ned,neko,nfm,nk,nls,nqc,nsh," +
                "nsi,nupkg,nuspec,nvv,nw,nxc,o,oca,octest,odl,omo,owl,p,p3d,patch,pb,pbg,pbk,pbxbtree," +
                "pcp,pde,pdm,ph,pika,pjx,pkgdef,pkgundef,pl,pl1,plc,ple,pli,pm,po,pot,ppc,prg,prg,pri," +
                "pri,psc,ptl,pwn,pxd,pyd,pyw,pyx,qpr,r,r,rav,rbc,rbp,rc,rc2,rdlc,refresh,res,resjson," +
                "resources,resw,rise,rnc,rodl,rpy,rsrc,rss,rul,s,s19,sas,sb,sb2,sbproj,sc,scc,scriptsuite," +
                "scriptterminology,slogo,sltng,sma,smali,snippet,so,spec,sqlproj,src,rpm,ss,ssc,ssi," +
                "storyboard,sud,suo,sup,svn-base,swc,swd,sym,t,targets,tcl,tds,testrunconfig,testsettings," +
                "textfactory,tk,tld,tlh,tli,tmlanguage,tmproj,tns,tpu,tt,tu,tur,twig,ui,uml,v,vbg,vbp," +
                "vbx,vbz,vc,vcp,vdm,vdp,vgc,vhd,vm,vsmacros,vsmdi,vsmproj,vsp,vsps,vspscc,vspx,vssscc," +
                "vsz,vtml,vtv,w,w32,wdgt,wdgtproj,wdl,wdp,wdw,wiq,wixlib,wixmsp,wixmst,wixobj,wixout," +
                "wixpdb,workspace,wpw,wsc,wsp,wxi,wxl,wxs,xaml,xamlx,xap,xcappdata,xcarchive,xcconfig," +
                "xcdatamodeld,xcsnapshots,xcworkspace,xib,xojo_binary_project,xojo_menu,xojo_project," +
                "xojo_xml_project,xoml,xpp,xql,xqm,xquery,xt,y,yaml,ymp,ypr,001,abc,acp,act,actx,ada," +
                "adb,adblock,alm,alp,alx,am,aml,aml,anjuta,applet,appxsym,apr,ascs,asdb,asx,aut,axe," +
                "basex,bpr,bsh,btn,buildpath,bytes,cap,car,cba,ccs,ccscc,cdxml,ckbx,cma,cpb,cst,ctc," +
                "cto,ctsym,ctx,dabriefcase,daconfig,ddd,ddm,ddp,ddx,defs,dep,dev,developerprofile,dfk," +
                "docset,dox,dres,dsk,dsym,dylib,eba,ecp,edm,el,elc,fbp7,fce,fcl,fd,feature,filters," +
                "fpp,fpt,frj,frm,frx,fsl,gar,gbap,gbas,gbm,gbr,gbs,gdfmakerproject,glade,gls,gml,go," +
                "gpj,gvy,handlebars,hhh,hrl,hydra,i,ipp,isc,iwz,j,jav,jed,jl,jnilib,jpd,jsh,jss,jsxinc," +
                "lis,list,lol,lrdb,lsp,m,make,makefile,mako,md,mdown,med,mfcribbon-ms,mlb,mode1v3," +
                "mode2v3,mvx,nim,orderedtest,os,osc,oxygene,p,p6,pbproj,pc,pde,pdl,perspective,perspectivev3," +
                "pfg,pkproj,plg,pltsuite,pmq,pom,ppl,ppu,prg,project,psd1,psess,pxi,pym,qml,qx,r,rb," +
                "rbm,rbw,rbxs,rdoc,reb,rls,rotest,rotestresult,rs,rwsnippet,sb,sbr,sdl,sed,sem,set," +
                "sex,sgpbpr,sgpsc,sll,slogt,smf,spt,spt,tcc,tiprogram,tmpl,tmproject,umlclass,vala,var," +
                "vc4,vcx,vdm,vic,vpc,vsct,vsixmanifest,vsl,vspf,wid,winmd,worksheet,wowproj,xbf,xcdatamodel," +
                "xcode,xojo_binary_menu,xojo_binary_toolbar,xojo_binary_window,xojo_toolbar,xojo_window," +
                "xojo_xml_menu,xojo_xml_toolbar,xojo_xml_window,xqy,xsx,yab,$01,ab,arr,art,b,bur,cdx," +
                "cham,chef,ci,cxt,depend,dis,drf,exl,fimpp,global,gm,gnumakefile,gpj,hcf,jsh,kb,kdevdlg," +
                "kdmp,lit,lrf,markdn,ow,pickle,pnt,prg,pty,rbvcp,rdoc_options,ru,scratch,setup,sjava," +
                "sml,tpx,vac,x,yml2").split(","));
        for(String s: imageExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }


    private static boolean isImage(String ext)
    {
        List<String> imageExts = Arrays.asList(("bmp,dds,gif,jpg,png,psd,pspimage,tga,thm,tif,tiff," +
                "yuv,abm,afx,cpg,cpt,dcm,dib,dpx,dt2,hdp,ipx,itc2,jp2,jpeg,jps,jpx,max,mng,mpo,mxi," +
                "pictclipping,ppm,psp,pspbrush,pvr,pxm,sdr,sid,skm,thm,tif,wb1,wbc,wbd,wbz,xcf,2bp," +
                "360,accountpicture-ms,acorn,agif,agp,apd,apng,apx,art,asw,avatar,avb,awd,blkrt,bm2," +
                "bmc,bss,can,cd5,cdg,cin,cit,colz,cpc,cps,csf,djvu,dm3,dmi,dtw,dvl,ecw,epp,exr,fits," +
                "fpos,fpx,gbr,gcdp,gih,gim,hdr,hdrp,hpi,i3d,info,ithmb,iwi,j2c,jb2,jbig2,jbr,jia,jng," +
                "jpc,jxr,kdi,lb,lif,lzp,mat,mbm,mix,mnr,mpf,mrxs,msp,myl,ncd,oc3,oc4,oc5,oci,omf,oplc" +
                ",ora,ota,ozb,pano,pat,pbm,pcd,pcx,pdd,pdn,pe4,pe4,pgf,pgm,pi2,pic,pic,picnc,pict," +
                "pixadex,pmg,pnm,pns,pov,ppf,prw,psb,psdx,pse,psf,ptg,px,pxd,pxr,pza,pzp,pzs,qmg,qti," +
                "qtif,ras,rif,rle,rli,rpf,rvg,s2mv,sai,sct,sig,skitch,spa,spe,sph,spj,spp,spr,sup,tbn," +
                "tex,tg4,thumb,tjp,tn,tpf,tps,vpe,vrphoto,vss,wbmp,webp,xpm,zif,73i,8xi,9,png,aic,ais," +
                "apm,aps,awd,bmf,bmx,bmz,brn,brt,bti,c4,cal,cals,cdc,cimg,cpbitmap,cpd,cpx,ct,dc2,dcx," +
                "ddt,dgt,dicom,djv,fax,fil,frm,gfie,ggr,gmbck,gmspr,gp4,gpd,gro,ica,icn,icon,icpr,ilbm," +
                "ink,int,ipick,ivr,j2k,jas,jbf,jfi,jfif,jif,jpd,jpe,jpf,jpg2,jtf,jwl,kic,kpg,lbm,ljp,mac," +
                "mic,msk,ncr,nct,odi,otb,oti,ozj,ozt,pap,pc3,pfi,pfr,pix,pjpg,pm,pni,pnt,pp4,pp5,pts,ptx," +
                "ptx,pwp,pxicon,rcu,rgb,rgf,ric,riff,rri,rsb,rsr,sbp,scn,sfc,sfw,sgi,shg,skypeemoticonset," +
                "sld,sprite,sumo,sun,sva,svm,t2b,tfc,tm2,tub,ufo,uga,vda,vic,viff,vst,wbm,wdp,wi,wpb," +
                "wpe,wvl,xbm,xwd,y,ysp,001,411,8pbs,acr,adc,albm,arr,artwork,arw,blz,brk,cam,ce,cut,ddb," +
                "drz,fac,face,fal,fbm,fpg,g3,gfb,grob,gry,hf,hr,hrf,ic1,ic2,ic3,icb,img,imj,iphotoproject," +
                "ivue,j,jbig,jbmp,jiff,kdk,kfx,kodak,mbm,mcs,met,mip,mrb,neo,nlm,pac,pal,pc1,pc2,pi1," +
                "pi2,pi3,pi4,pi5,pi6,pic,pix,pjpeg,pm3,pntg,pop,pov,ptk,qif,rcl,rgb,rix,rs,sar,scg,sci," +
                "scp,scu,sep,sff,sim,smp,sob,spc,spiff,spu,sr,ste,suniff,taac,tb0,tn1,tn2,tn3,tny,tpi," +
                "trif,u,urt,usertile-ms,v,vff,vna,wic,wmp,ai,eps,ps,svg,asy,cdd,cdmm,cdr,cgm,cvx,drw," +
                "emf,emz,fxg,graffle,hpl,plt,svgz,vsd,vsdx,xar,artb,cdmt,cdmtz,cdmz,cil,clarify,cmx," +
                "csy,cv5,cvg,cvi,dcs,design,dhs,dia,dpp,dpr,drawing,drw,dxb,egc,ep,epsf,ezdraw,fh10," +
                "fh11,fh9,fig,fs,gdraw,gstencil,hgl,hpg,hpgl,idea,igx,lmk,mgcb,mgmf,mgmx,mp,odg,pat,pen," +
                "pl,plt,rdl,scv,sk2,sketch,slddrt,snagitstamps,snagstyles,sxd,tlc,tne,ufr,vbr,vml,vsdm," +
                "vst,vstm,vstx,wmf,wmz,wpg,xmind,xmmap,abc,ac5,ac6,af3,art,awg,cag,ccx,cdt,cdx,cdx,cnv," +
                "cor,cvs,cwt,ddrw,ded,dpx,drawit,dsf,fh6,fh7,fh8,fhd,fif,fmv,ft11,ftn,gem,glox,gls,gsd," +
                "gtemplate,igt,ink,mgc,mgmt,mgs,mgtx,mmat,otg,ovp,ovr,psid,sda,sk1,smf,ssk,std,stn,svf," +
                "tpl,vec,xpr,yal,af2,cxf,fh3,fh4,fh5,ft10,ft7,ft8,ft9,gks,imd,ink,nap,pcs,pd,pfd,pfv," +
                "pmg,pobj,pws,zgm").split(","));
        for(String s: imageExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    private static boolean isLib(String ext)
    {
        List<String> imageExts = Arrays.asList(("so, dll, exe").split(","));
        for(String s: imageExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }


    private static boolean isDoc(String ext)
    {
        List<String> textExts = Arrays.asList(("doc,docx,odt,rtf").split(","));
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }


    private static boolean isText(String ext)
    {
        List<String> textExts = Arrays.asList(("log,msg,pages,tex,txt,wpd,wps,abw,bib," +
                "dotx,dwd,eml,fdx,gdoc,lst,sig,sty,wps,wpt,aim,ans,asc,ase,aww,bad,bdp,bdr,bean,bib,bna," +
                "boc,btd,bzabw,charset,chord,cnm,crwl,cyi,diz,dne,doc,docm,dotm,dvi,dx,eio,emlx," +
                "emulecollection,err,etf,fadein,fbl,fcf,fdr,fdt,fdxt,flr,fodt,fountain,fpt,frt,gpd,gsd," +
                "gthr,gv,hht,hs,hwp,idx,ipspot,kes,klg,klg,knt,kon,kwd,lbt,lis,lnt,lp2,lst,ltx,luf,lwp," +
                "lxfml,lyx,mbox,md5,txt,mell,mellel,mnt,mwd,mwp,nb,nfo,njx,notes,nwp,ofl,ott,p7s,pages-tef," +
                "pjt,plantuml,psw,pu,pwd,pwi,qdl,qpf,readme,ris,rpt,rst,rtd,rtfd,rzk,rzn,safetext,scriv," +
                "scrivx,sct,scw,sdw,sgm,sla,sla,gz,sms,ssa,story,strings,sub,sublime-project,sublime-workspace," +
                "sxw,tab,tdf,tdf,template,text,textclipping,tmd,u3i,unauth,unx,uot,utf8,utxt,vct,webdoc," +
                "wpa,wpd,xbdoc,xdl,xdl,xwp,xwp,xy3,xyp,xyw,zabw,zrtf,1st,act,apt,asc,aty,awp,awt,bbs,bml," +
                "brx,chart,cod,cws,dgs,dropbox,dsv,dxb,dxp,eit,emf,epp,err,fadein,template,fdf,fds,gmd," +
                "gpn,hbk,hz,jis,min,mw,ndoc,ngloss,nwctxt,nwm,ocr,odm,ort,pfs,pfx,pmo,prt,pvj,pvm,pwdp," +
                "pwdpl,pwr,rtx,run,sam,sam,scc,scm,sdm,se,session,skcard,smf,stw,sxg,tab,tlb,tm,tmv,tpc," +
                "trelby,tvj,uof,vnt,wbk,wp,wp4,wp5,wp7,wpt,wri,xbplate,ascii,dca,docxml,docz,dox,dsc," +
                "etx,euc,faq,fft,fwdn,hwp,iil,ipf,jarvis,joe,jp1,jrtf,latex,ltr,lue,lyt,man,mcw,me,now," +
                "odif,odo,openbsd,prt,rad,rft,saf,save,sdoc,thp,upd,vw,wn,wp6,wpd,wpl,wpw,wsd,wtx,xwp," +
                "xy,zw,prop,rc,conf,bak,idc,kcm,kl").split(","));
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    private static boolean isVideo(String ext)
    {
        List<String> textExts = Arrays.asList(("3g2,3gp,asf,asx,avi,flv,m4v,mov,mp4,mpg,rm,srt,swf,vob," +
                "wmv,aepx,ale,avp,avs,bdm,bik,bin,bsf,camproj,cpi,dash,divx,dmsm,dream,dvdmedia,dvr-ms," +
                "dzm,dzp,edl,f4v,fbr,fcproject,hdmov,imovieproj,ism,ismv,m2p,mkv,mod,moi,mpeg,mts,mxf," +
                "ogv,otrkey,pds,prproj,psh,r3d,rcproject,rmvb,scm,smil,snagproj,sqz,stx,swi,tix,trp,ts," +
                "veg,vf,vro,webm,wlmp,wtv,xvid,yuv,3gp2,3gpp,3p2,890,aaf,aec,aep,aetx,ajp,amc,amv,amx," +
                "arcut,arf,avb,avchd,avv,axm,bdmv,bdt3,bmc,bmk,camrec,ced,cine,cip,clpi,cmmp,cmmtpl," +
                "cmproj,cmrec,cst,d2v,d3v,dat,dce,dck,dcr,dcr,dir,dmsd,dmsd3d,dmss,dmx,dpa,dpg,dv-avi," +
                "dvr,dvx,dxr,dzt,evo,eye,ezt,f4p,fbz,fcp,flc,flh,fli,fpdx,ftc,gcs,gfp,gts,hdv,hkm,ifo," +
                "imovieproject,ircp,ismc,ivr,izz,izzy,jss,jts,jtv,kdenlive,lrv,m1pg,m21,m21,m2t,m2ts," +
                "m2v,mani,mgv,mj2,mjp,mk3d,mnv,mp21,mp21,mpgindex,mpl,mpls,mproj,mpv,mqv,msdvd,mse," +
                "mswmm,mtv,mvd,mve,mvp,mvp,mvy,mxv,ncor,nsv,nuv,nvc,ogm,ogx,pac,pgi,photoshow,piv,plproj," +
                "pmf,ppj,prel,pro,prtl,pxv,qtl,qtz,rcd,rdb,rec,rmd,rmp,rms,roq,rsx,rum,rv,rvid,rvl,sbk," +
                "scc,screenflow,sdv,sedprj,seq,sfvidcap,siv,smi,smi,smk,stl,svi,swt,tda3mt,thp,tivo," +
                "tod,tp,tp0,tpd,tpr,trec,tsp,ttxt,tvlayer,tvs,tvshow,usf,usm,vbc,vc1,vcpf,vcv,vdo,vdr," +
                "vep,vfz,vgz,viewlet,vlab,vp6,vp7,vpj,vsp,wcp,wmd,wmmp,wmx,wp3,wpl,wve,wvx,xej,xel,xesc," +
                "xfl,xlmv,y4m,zm1,zm2,zm3,zmv,264,3gpp2,3mm,60d,aet,avc,avd,avs,awlive,bdt2,bnp,box,bs4," +
                "bu,bvr,byu,camv,clk,cx3,dav,ddat,dif,dlx,dmb,dmsm3d,dnc,dv4,f4f,fbr,ffd,flx,gvp,h264," +
                "inp,int,irf,iva,ivf,jmv,k3g,ktn,lrec,lsx,lvix,m1v,m2a,m4u,meta,mjpg,modd,moff,moov," +
                "movie,mp2v,mp4,infovid,mp4v,mpe,mpl,mpsub,mvc,mvex,mys,osp,par,playlist," +
                "pns,pro4dvd,pro5dvd,proqc,pssd,pva,pvr,qt,qtch,qtindex,qtm,rp,rts,sbt,scn,sfd,sml,smv," +
                "spl,str,tdt,tid,tvrecording,vcr,vem,vft,vfw,vid,video,vix,vs4,vse,w32,wm,wot,xmv,yog," +
                "787,am,anim,aqt,bix,cel,cvc,db2,dsy,gl,gom,grasp,gvi,ismclip,ivs,kmv,lsf,m15,m4e,m75," +
                "mmv,mob,mpeg1,mpeg4,mpf,mpg2,mpv2,msh,mvb,nut,orv,pjs,pmv,psb,rmd,rmv,rts,scm,sec,ssf," +
                "ssm,tdx,vdx,viv,vivo,vp3,zeg").split(","));//TODO split this manually for better performance
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    private static boolean isDb(String ext)
    {
        List<String> textExts = Arrays.asList(("accdb,db,dbf,mdb,pdb,sql,adp,cdb,dsn,fmp12,fp7,frm,mdf," +
                "mwb,ora,pdb,qvd,sqlite,sqlitedb,te,tps,$er,4dd,accdc,accde,accdr,accdt,accft,adb,ade," +
                "adf,alf,ask,cdb,cdb,ckp,cpd,daconnections,dacpac,daschema,db,db3,dbc,dbs,dbt,dbv,dcb," +
                "dcx,ddl,dp1,dtsx,dxl,eco,ecx,edb,fdb,fic,fmpsl,fpt,gdb,gwi,hdb,ib,idb,ihx,itdb,itw," +
                "kdb,kexi,lgc,maq,marshal,mav,mpd,myd,ndf,nrmlib,nsf,nv2,nyf,odb,oqy,p96,p97,pan,pdm," +
                "pnz,qry,rsd,scx,sdb,sdb,sdb,sdf,sis,spq,sqlite3,trc,udb,udl,usr,vis,wdb,xdb,4dl," +
                "abcddb,abs,abx,accdw,adb,adn,btr,cat,cma,dad,dadiagrams,db-shm,db-wal,dbx,dct,dqy,dsk," +
                "fp5,gdb,his,jtx,kexic,kexis,maf,mar,mas,maw,mdt,mrg,mud,ns3,ns4,nv,odb,orx,owc,rbf,rod," +
                "rpd,sbf,sdb,teacher,tmd,trc,trm,vpd,wmdb,wrk,^^^,db2,fcd,fm5,fmp,fol,fp3,fp4,mdbhtml," +
                "mdn,ns2,rctd,v12,xld").split(","));
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    private static boolean isArchive(String ext)
    {
        List<String> textExts = Arrays.asList(("7z,cbr,deb,gz,pkg,rar,rpm,sitx,tar,gz,zip,zipx,alz,bz," +
                "bz2,cbz,mpkg,pet,sfx,sit,tgz,war,0,7z,001,7z,002,ace,apz,ar,arc,archiver,asr,b1,b64,ba," +
                "bndl,bzip,c00,c01,cba,cbt,comppkg,hauptwerk,rar,cp9,cpgz,cxarchive,czip,dar,dgc,dist," +
                "dl_,ecs,efw,egg,f,gca,gmz,gzip,hbc,hki,hki1,hki2,hki3,ice,ipg,ipk,ita,jgz,jic,kgb," +
                "layout,lbr,lha,lnx,lqr,lz,lzm,lzma,lzo,mint,mzp,mzp,nex,package,pae,pak,paq6,paq7,par," +
                "par2,pbi,pea,pf,piz,psz,pup,pup,pwa,qda,r00,r01,rar5,rp9,rte,rz,s00,s01,s02,s7z,sar," +
                "sdc,sea,sfs,sh,shr,smpf,sqx,tar,lzma,taz,tbz,tz,uha,vem,vsi,xar,xef,xmcdz,z,z01," +
                "zfsendtotarget,zz,a00,a01,a02,agg,arh,arj,bh,boo,bundle,c02,c10,cb7,cdz," +
                "comppkg_hauptwerk_rar,cpt,dd,dz,epi,fdp,fp8,gz2,gzi,hbc2,hbe,ize,lemon,lzh,lzx,mou,oar," +
                "oz,p01,p19,pack,gz,paq8,paq8l,pax,pcv,puz,r0,r02,r03,r1,r2,r21,r30,rev,rk,rnc,sbx,sdn," +
                "sen,shar,snb,srep,sy_,tar,gz2,tar,xz,tbz2,tlz,tlzma,trs,txz,tx_,ufs,uzip,uzip,xx,xz,y," +
                "z02,z03,z04,zix,zoo,zsplit,000,ain,ari,ark,bza,bzip2,car,gza,ha,hpk,hyp,ish,j,jar,pack," +
                "kz,md,paq8f,paq8p,pim,pit,shk,spt,tg,uc2,wot,xez,yz,yz1,zap,zi,zl,zpi").split(","));
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    private static boolean isWeb(String ext)
    {
        List<String> textExts = Arrays.asList(("asp,aspx,cer,cfm,csr,htm,html,jsp,rss,xhtml,a5w,alx,asax," +
                "asmx,atom,att,axd,chm,crt,cshtml,dwt,fcgi,htaccess,jlp,jsf,jso,jspx,zb,opml,p12,pac," +
                "qbo,spc,ucf,webarchive,wgt,wml,wsdl,xfdl,xhtm,a4p,adr,aex,a,ap,appcache,aro,asa,ascx," +
                "ashx,asr,awm,bml,bok,browser,btapp,cha,chat,codasite,co,crl,dap,dcr,der,dhtml,disco," +
                "discomap,dll,do,dowload,edge,epibrw,esproj,ewp,fwp,ge,gsp,htx,hxs,hype,idc,iwdgt,jhtml," +
                "jws,kit,lasso,lbc,less,maff,mht,mhtml,mspx,muse,mvc,od,oam,obml,ogc,olp,oth,p7b,p7c,pem," +
                "pro,psp,pub,qf,rflw,rhtml,rjs,rt,rwp,rwsw,saveddeck,scss,shtm,shtml,sitemap,sites,sites2," +
                "srf,stc,suck,swz,tvpi,tvvi,url,vbd,vbhtml,vdw,vrml,vsdisco,wdgt,web,webbookmark," +
                "webhistory,webloc,website,whtt,woa,wpp,wrf,xbel,xht,xpd,xul,zfo,zhtml,zhtml,zul,zvz," +
                "ccbjs,cfml,cpg,dbm,dml,ece,fmp,hdml,htc,iqy,itms,itpc,jcz,jspa,jss,jst,mapx,master," +
                "xg,p7,page,phtml,prf,ptw,qrm,rw3,rwtheme,seam,sht,site,ssp,stm,stp,svc,svr,uhtml,wbs," +
                "wbxml,webarchivexml,wgp,widget,w,wpx,xbl,xss,xws,bwp,cdf,cms,compressed,csp,dochtml," +
                "docmhtml,dothtml,hdm,jvs,map,moz,mvr,phtm,ppthtml,pptmhtml,sdb,stl,stml,vlp,vrt").split(","));
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

    private static boolean isAudio(String ext)
    {
        List<String> textExts = Arrays.asList(("aif,iff,m3u,m4a,mid,mp3,mpa,ra,wav,wma,aa,aa3,acd,acd-zip," +
                "acm,afc,als,amr,amxd,amz,ape,at3,caf,cda,cpr,dcf,dmsa,dmse,dss,emp,emx,flac,ftm,gpx,logic," +
                "m3u8,m4b,m4r,midi,mod,mxl,nbs,nki,nra,ogg,omf,pcast,pls,ptf,ptx,ram,rns,rx2,seq,sesx,sib," +
                "slp,snd,trak,3ga,4mp,5xb,5xe,5xs,8svx,a2b,a2i,a2m,aac,aax,abc,abm,ac3,acd-bak,act,adg,adt," +
                "adts,agm,agr,aifc,aiff,akp,alc,amf,ams,ams,aob,apl,asd,au,aud,aup,band,bap,bdd,bidule,bnk," +
                "bun,bwf,bww,caff,cdda,cdlx,cdo,cdr,cel,cfa,cgrp,cidb,ckb,conform,copy,cpt,csh,cts,cwb,cwp," +
                "cwt,dcm,dct,dewf,df2,dfc,dig,dig,dls,dm,dmf,dra,drg,ds2,dsf,dsm,dtm,dts,dtshd,dvf,dwd,efa," +
                "efk,efq,efs,efv,emd,esps,f2r,f32,f3r,f4a,f64,fdp,fev,flp,fpa,frg,fsb,fsm,ftm,ftmx,fzf,fzv," +
                "g721,g723,g726,gbs,gig,gp5,gpbank,gpk,groove,gsf,gsflib,gsm,h4b,h5b,h5e,h5s,hbe,hsb," +
                "ics,igp,ins,isma,iti,k26,kar,kfn,koz,koz,krz,ksf,kt3,la,lso,lwv,m4p,ma1,mbr,mdc,med," +
                "mgv,minigsf,miniusf,mka,mmf,mmm,mmp,mmpz,mo3,mp2,mpc,mpdp,mpga,mscz,mte,mtf,mti,mtm," +
                "mtp,mts,mus,mus,musx,mux,mx5,mxmf,myr,narrative,ncw,nkb,nkc,nkm,nks,nkx,nml,nmsv,note," +
                "nrt,nsa,nsf,nst,ntn,nwc,obw,odm,oga,okt,oma,omg,omx,ots,ove,ovw,pandora,pca,pcg,peak," +
                "pek,pk,pkf,pla,ply,pna,psf,psm,ptm,pts,qcp,r1m,rax,rbs,rex,rfl,rgrp,rip,rmi,rmj,rmx," +
                "rng,rol,rsn,rso,rti,s3i,s3m,sap,sbi,sc2,scs11,sd,sd,sd2,sdat,sds,ses,sf2,sfk,sfl,sfpack," +
                "sgp,shn,slx,sma,smf,smp,smpx,snd,sng,sou,sppack,sprg,sseq,stap,stm,stx,sty,svd,swa," +
                "sxt,syh,syn,syw,syx,tak,td0,tg,tta,txw,u,uax,ult,uni,usf,usflib,ust,uw,uwf,vag,vap," +
                "vc3,vlc,vmd,vmo,voc,vox,voxal,vpl,vpm,vpw,vqf,vrf,vsq,vyf,w01,w64,wave,wax,wfb,wfd," +
                "wfm,wfp,wow,wpk,wpp,wproj,wrk,wus,wut,wv,wvc,wve,wwu,xa,xfs,xm,xmu,xrns,xspf,yookoo," +
                "zpl,zvd,669,a2p,a2t,a2w,ab,acp,adv,ahx,aimppl,ais,alaw,all,apf,aria,ariax,ase,au," +
                "avastsounds,awb,ay,b4s,bmml,brstm,bwg,c01,ckf,cmf,dff,djr,dmc,ds,dw,efe,emy,eop,erb," +
                "expressionmap,far,fls,gbproj,h0,h3b,h3e,h4e,hbb,hbs,hdp,hma,hps,iaa,igr,imp,ins,it," +
                "itls,its,jam,jam,kit,kmp,kpl,ksc,ksd,kt2,l,lof,lqt,lvp,m,m1a,m2,mdl,minipsf,minipsf2," +
                "mlp,mmp,mogg,mp1,mpu,mp_,mscx,msv,mt2,mu3,mui,mus,mux,mx3,mx4,mx5template,mzp,npl,nvf," +
                "ofr,opus,ovw,pac,pbf,pcm,pho,phy,pjunoxl,plst,pno,ppc,ppcx,prg,psf1,psf2,psy,ptcop," +
                "pvc,q1,q2,rad,raw,rbs,rcy,rmm,rsf,rta,rts,rvx,s3z,saf,sbg,sbk,sd2f,sdt,sfap0,sfs,sid," +
                "smp,snd,sng,sns,sph,spx,sseq,ssnd,sty,svx,tak,thx,toc,tsp,ub,ulaw,v2m,vb,vdj,vgm,vgz," +
                "vmf,vmf,vtx,wav,wem,wtpl,wtpt,xa,xbmml,xmf,xmi,xmz,xpf,xsb,xsp,xwb,zpa,2sf,2sflib,6cm," +
                "8cm,8med,a52,al,alac,atrac,avr,bcs,bonk,box,cfxr,d00,d01,ddt,dsp,dwa,ear,evr,fda,fff," +
                "fzb,gio,gio,gm,gro,gsm,hmi,imf,ins,jo,jo-7z,k25,kin,ksm,ktp,mini2sf,minincsf,mt9,musa," +
                "muz,mwand,mws,nap,orc,pat,pd,pmpl,prg,r,record,rmf,rtm,sam,sb,sdii,sdx,seg,sf,snsf,sth," +
                "sti,stw,sw,swav,syn,tfmx,tm2,tm8,tmc,tun,u8,ulw,val,voi,wand,wyz,xi,xp,xt,ym,zab," +
                "zvr").split(","));
        for(String s: textExts)
        {
            if(s.equalsIgnoreCase(ext)) return true;
        }
        return false;
    }

}
