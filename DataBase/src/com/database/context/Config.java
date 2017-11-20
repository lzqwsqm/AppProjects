package com.database.context;


public final class Config
{
  public static final String ACCOUNT = "account";
  public static final String ASSET = "asset";
  public static final String AUDIT = "audit";
  public static final String BANK = "bank";
  public static final String BOND = "bond";
  public static final String BUDGET = "budget";
  public static final String CREDIT = "credit";
  public static final String CREDITAUDIT = "creditaudit";
  public static final String DEPOSIT = "deposit";
  public static final String EVECTION = "evection";
  public static final String EVECTIONAUDIT = "evectionaudit";
  public static final String EVECTIONKM = "evectionkm";
  public static final String FAVOR = "favor";
  public static final String FAVORTYPE = "favortype";
  public static final String FEEDBACK = "feedback";
  public static final String FRIEND = "friend";
  public static final String FRIENDTYPE = "friendtype";
  public static final String FUNDS = "funds";
  public static final String INDEX = "index";
  public static final String INSURANCE = "insurance";
  public static final String INTERESTRATE = "interestrate";
  public static final String INVEST_ACCOUNT = "investaccount";
  public static final String INVEST_AUDIT = "investaudit";
  public static final String INVEST_PROFIT = "investprofit";
  public static final String INVEST_TYPE = "investtype";
  public static final String KM = "km";
  public static final String MEMBER = "member";
  public static final String MEMO = "memo";
  public static final byte NETWORK_CMWAP = 1;
  public static final byte NETWORK_CTWAP = 3;
  public static final byte NETWORK_OTHER = 0;
  public static final byte NETWORK_UNIWAP = 2;
  public static final String NOTE = "note";
  public static final String PARAM = "param";
  public static final String PASSWORDBOX = "passwordbox";
  public static final String PLAN = "plan";
  public static final String REGISTER = "register";
  public static final String REPORT = "report";
  public static final String SAFETYPE = "safetype";
  public static final String STOCK = "stock";
  public static final String VIREMENT = "virement";
  public static final String[] adage;
  public static final String[] bless;
  public static byte main_page_title = 0;

  static
  {
	  bless = new String[4];
	  bless[0] = "你不理财 财不理你";
	  bless[1] = "君子爱财 十年不晚";
	  bless[2] = "财富生活 尽在掌控";
	  bless[3] = "用之有度 理之有方";
    
	  adage = new String[33];
	  adage[0] = "收入-储蓄=消费 ? \n收入-消费=储蓄 ?";
	  adage[1] = "合理使用信用卡\n避免过度透支消费";
	  adage[2] = "停止损失 保护本金\n让利润跑起来";
	  adage[3] = "习惯于理财\n就像习惯于消费";
	  adage[4] = "投资有风险\n小心又谨慎";
	  adage[5] = "不要因为表面的收益\n而购买保险产品";
	  adage[6] = "基金投资\n同样需要谨慎选择";
	  adage[7] = "鸡蛋不要放在一个篮子里\n也不要放在太多篮子里";
	  adage[8] = "合理规划资产配置\n就象规划你的职业";
	  adage[9] = "参考专家的建议\n而不是直接采纳";
	  adage[10] = "理财只能靠自己\n他人的建议不可尽信";
	  adage[11] = "预留一定的备付资金\n以便应付不时之需";
	  adage[12] = "投资是随着时间赚到钱\n 而不是明天赚到钱";
	  adage[13] = "理财需要知识 勇气 \n资金 计划书 耐心";
	  adage[14] = "保持耐心等待最佳时机\n不要轻易投入你的资金";
	  adage[15] = "金钱具有时间价值\n但需要通过理财来实现";
	  adage[16] = "你不理财 财不理你\n君子爱财 理之有道";
	  adage[17] = "主动储蓄 理性消费\n稳健投资 快乐理财";
	  adage[18] = "培养理财习惯\n把理财作为第二天性";
	  adage[19] = "设立资产平衡日\n对资产进行评估和调整";
	  adage[20] = "对自己的财务状况\n应做到心中有数";
	  adage[21] = "负债并不是财富\n相反可能带来风险";
	  adage[22] = "理财象马拉松一样漫长\n需要有计划 耐心 纪律";
	  adage[23] = "不要忘记收回你的投资\n因为情况不会永远保持";
	  adage[24] = "注意投资的成本\n虽然不多也能节省";
	  adage[25] = "习惯于思考财务问题\n而不是晚上的电视剧";
	  adage[26] = "对未来的较大开销\n提前准备一个专用账户";
	  adage[27] = "开一个零存整取账户\n每月存入部分工资";
	  adage[28] = "投资不是赌博\n要靠周密分析而不是运气";
	  adage[29] = "智商 情商 财商\n一个都不能少";
	  adage[30] = "先了解自己\n再了解理财";
	  adage[31] = "理财不能只关注收益率\n兼顾周期 流动性 安全性";
	  adage[32] = "不同的人生阶段需要\n不同的理财规划和目标";
    
  }
}

/* Location:           M:\可用文件\反编译\Output\君子爱财(2.0)\classes_dex2jar.jar
 * Qualified Name:     com.tomoney.finance.context.Config
 * JD-Core Version:    0.6.0
 */
