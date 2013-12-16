package nissen441.scripts.woodcutter;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.powerbot.event.MessageEvent;
import org.powerbot.event.MessageListener;
import org.powerbot.event.PaintListener;
//import org.powerbot.script.AbstractScript;
import org.powerbot.script.Manifest;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.Item;


//@Manifest(name = "FirstBot",version = 1.0,vip = false, topic = 1, authors = {"Nissen441"}, description = "Just a test script by Nissen441/GamehubDev")
//@Manifest(name = "First script", authors = "Nissen441", description = "Cuts wood.")
@Manifest(name = "NCutter", description = "Chops wood.")
public class Test extends PollingScript implements MessageListener, PaintListener{
	private List<Task> taskList = new ArrayList<>();
	private int logs = 0;
		
	
	
	@Override
	public void start(){
		taskList.add(new Chop(getContext()));
		taskList.add(new Drop(getContext()));
	}
	
	@Override
	public void messaged(MessageEvent e){
		String msg = e.getMessage().toLowerCase();
		if(msg.contains("You get some")){
			logs++;
		}
	}
	
	@Override
	public void repaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Tahoma", Font.BOLD, 16));
		g.drawString("Logs cut: " + logs, 50, 50);
	}

	
	
	
	public int poll() {
		for (Task task : taskList){
			if (task.activate()){
				task.execute();
				return 500;
			}
		}
		return 500;
	}
	
	
	public class Drop extends Task{

		private int logId = 1519;
		
		public Drop(MethodContext ctx) {
			super(ctx);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean activate() {
			// TODO Auto-generated method stub
			return ctx.backpack.select().count() == 28;
		}

		@Override
		public void execute() {
			for(Item i : ctx.backpack.id(logId)){
				i.interact("Drop");
				sleep(350, 500);
			}
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public class Chop extends Task{
private int [] treeIds = {38616, 38627, 58006};

		public Chop(MethodContext ctx) {		
			super(ctx);
			// TODO Much to be told
		}

		@Override
		public boolean activate() {
			return ctx.backpack.select().count() < 28
					&& !ctx.objects.select().id(treeIds).isEmpty()
	                && ctx.players.local().getAnimation() == -1;
		}

		@Override
		public void execute() {
			
			GameObject tree = ctx.objects.nearest().poll();
			if (tree.isOnScreen()){
				tree.interact("chop");
			} else {
				ctx.movement.stepTowards(tree);
				ctx.camera.turnTo(tree);
			}
			// TODO Nothing
			
		}
		
	}

	public abstract class Task extends MethodProvider{
		public Task(MethodContext ctx) {
			super(ctx);
			// TODO Cool
		}
		public abstract boolean activate();
		public abstract void execute();
		
	}


	
	
	
	
	
}
	

	
		
	
	
