package scripts.easyslayer;

import org.tribot.api.Clicking;
import org.tribot.api2007.Banking;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Skills;
import org.tribot.api2007.Skills.SKILLS;
import org.tribot.api2007.types.RSItem;

public class TabChecking{
	//TODO checkMagicLevel
		public static void checkMagicLevel(){
			int lv = Skills.getCurrentLevel(SKILLS.MAGIC);
			if(!TuraelSlayer.useTabs){
				if (lv < 31 && lv >= 25){
					TuraelSlayer.useTabV = false;
				}
				else if (lv < 37){
					TuraelSlayer.useTabV = false;
					TuraelSlayer.useTabL = false;
				}
				else if (lv < 45){
					TuraelSlayer.useTabV = false;
					TuraelSlayer.useTabL = false;
					TuraelSlayer.useTabF = false;
				}
				else if (lv < 51){
					TuraelSlayer.useTabV = false;
					TuraelSlayer.useTabL = false;
					TuraelSlayer.useTabF = false;
					TuraelSlayer.useTabC = false;
				}
				else if (lv >= 51){
					TuraelSlayer.useTabV = false;
					TuraelSlayer.useTabL = false;
					TuraelSlayer.useTabF = false;
					TuraelSlayer.useTabC = false;
					TuraelSlayer.useTabA = false;
				}
				else
					TuraelSlayer.useTabs = true;
			}
		}

		public static void withdrawTabOrRune(int destination){ // 0 is V, 1 is F, 2 is C, 3 is L, 4 is A
			
			RSItem[] vtab = Inventory.find(TuraelSlayer.VTAB);
			RSItem[] ftab = Inventory.find(TuraelSlayer.FTAB);
			RSItem[] ctab = Inventory.find(TuraelSlayer.CTAB);
			RSItem[] ltab = Inventory.find(TuraelSlayer.LTAB);
			RSItem[] atab = Inventory.find(TuraelSlayer.ATAB);
			
			if(TuraelSlayer.useTabs){
				switch(destination){
				case 0:
					if(vtab.length == 0){
						Clicking.hover(Banking.find(TuraelSlayer.VTAB));
						Banking.withdraw(10, TuraelSlayer.VTAB);
					}
					break;
				case 1:
					if(ftab.length == 0){
						Clicking.hover(Banking.find(TuraelSlayer.FTAB));
						Banking.withdraw(10, TuraelSlayer.FTAB);
					}
					break;
				case 2:
					if(ctab.length == 0){
						Clicking.hover(Banking.find(TuraelSlayer.CTAB));
						Banking.withdraw(10, TuraelSlayer.CTAB);
					}
					break;
				case 3:
					if(ltab.length == 0){
						Clicking.hover(Banking.find(TuraelSlayer.LTAB));
						Banking.withdraw(10, TuraelSlayer.LTAB);
					}
					break;
				case 4:
					if(atab.length == 0){
						Clicking.hover(Banking.find(TuraelSlayer.ATAB));
						Banking.withdraw(10, TuraelSlayer.ATAB);
					}
					break;
				}
			}
			else{
				switch(destination){
				case 0:
					if(TuraelSlayer.useTabV){
						if(vtab.length == 0){
							Banking.withdraw(10, TuraelSlayer.VTAB);
						}
					}
					else {
						if(!haveRune(1)){
							Banking.withdraw(10, TuraelSlayer.LAW);
						}
						if(!haveRune(2)){
							Banking.withdraw(30, TuraelSlayer.AIR);
						}
						if(!haveRune(3)){
							Banking.withdraw(10, TuraelSlayer.FIRE);
						}
					}
					break;
				case 1: 
					if(TuraelSlayer.useTabF){
					if(ftab.length == 0){
						Banking.withdraw(10, TuraelSlayer.FTAB);
					}
					}
					else{
						if(!haveRune(1)){
							Banking.withdraw(10, TuraelSlayer.LAW);
						}
						if(!haveRune(2)){
							Banking.withdraw(10, TuraelSlayer.AIR);
						}
						if(!haveRune(4)){
							Banking.withdraw(10, TuraelSlayer.WATER);
						}
					}
					break;
				case 2:
					if(TuraelSlayer.useTabC){
					if(ctab.length == 0){
						Banking.withdraw(10, TuraelSlayer.CTAB);
					}
					}
					else {
						if(!haveRune(1)){
							Banking.withdraw(10, TuraelSlayer.LAW);
						}
						if (!haveRune(2)){
							Banking.withdraw(10, TuraelSlayer.AIR);
						}
					}
					break;
				case 3:
					if(TuraelSlayer.useTabL){
					if(ltab.length == 0){
						Banking.withdraw(10, TuraelSlayer.LTAB);
					}
					}
					else{ 
						if(!haveRune(1)){ 
							Banking.withdraw(10, TuraelSlayer.LAW);
						}
						if(!haveRune(2)){
							Banking.withdraw(10, TuraelSlayer.AIR);
						}
						if(!haveRune(0)){
							Banking.withdraw(10, TuraelSlayer.EARTH);
						}
					}
					break;
				case 4:
					
					if(TuraelSlayer.useTabA){
						if (atab.length == 0) {
							Banking.withdraw(10, TuraelSlayer.ATAB);
						}
					}
					else{ 
						if(!haveRune(1)){ 
							Banking.withdraw(10, TuraelSlayer.LAW);
						}
						if (!haveRune(4)){
							Banking.withdraw(10, TuraelSlayer.WATER);
						}
					}
					break;
				}
			}
		}
		
		public static boolean haveRune(int option){ 
			
			int earth = Inventory.getCount(TuraelSlayer.EARTH);
			int law = Inventory.getCount(TuraelSlayer.LAW);
			int air = Inventory.getCount(TuraelSlayer.AIR);
			int fire = Inventory.getCount(TuraelSlayer.FIRE);
			int water = Inventory.getCount(TuraelSlayer.WATER);
			
			switch(option){
			case 0:
				if(earth < 10)
					return false;
				break;
			case 1:
				if(law < 10)
					return false;
				break;
			case 2:
				if(air < 10)
					return false;
				break;
			case 3:
				if(fire < 10)
					return false;
				break;
			case 4:
				if(water < 10)
					return false;
				break;
			}
			return true;
			
		}
}


