package com.qq183311108.biz.vo;

import com.github.crab2died.annotation.ExcelField;

public class FrimwareInfoExcelVO
{
  @ExcelField(title="设备序列号", order=1)
  private String sn;
  @ExcelField(title="设备网卡地址", order=2)
  private String mac;
  @ExcelField(title="设备类型标识", order=3)
  private String typeName;
  @ExcelField(title="设备版本号", order=4)
  private String ver;
  @ExcelField(title="允许更新", order=5)
  private String flag;
  
  public String getSn()
  {
    return this.sn;
  }
  
  public void setSn(String sn)
  {
    this.sn = sn;
  }
  
  public String getMac()
  {
    return this.mac;
  }
  
  public void setMac(String mac)
  {
    this.mac = mac;
  }
  
  public String getTypeName()
  {
    return this.typeName;
  }
  
  public void setTypeName(String typeName)
  {
    this.typeName = typeName;
  }
  
  public String getVer()
  {
    return this.ver;
  }
  
  public void setVer(String ver)
  {
    this.ver = ver;
  }
  
  public String getFlag()
  {
    return this.flag;
  }
  
  public void setFlag(String flag)
  {
    this.flag = flag;
  }
}
