package nissen441.scripts.miner;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import org.powerbot.event.MessageEvent;
import org.powerbot.event.MessageListener;
import org.powerbot.event.PaintListener;
import org.powerbot.script.Manifest;
import org.powerbot.script.methods.MethodContext;
import org.powerbot.script.methods.MethodProvider;
import org.powerbot.script.wrappers.GameObject;
import org.powerbot.script.PollingScript;
import org.powerbot.script.wrappers.Item;

@Manifest (name = "GMiner", description = "Mines clay and iron")

public class MinerScript extends PollingScript implements MessageListener, PaintListener{

	private List<Task> taskList = new ArrayList<>();
	private int ores = 0;
	
	@Override
	public void start(){
		taskList.add(new Mine(getContext()));
		taskList.add(new Drop(getContext()));
	}
	
	
	public class Mine extends Task{
		private int[] oreIds = {11556, 15504, 11505};
		
		
		public Mine(MethodContext ctx) {
			super(ctx);
			// TODO Mining class
		}

		@Override
		public boolean active() {
			
			return ctx.backpack.select().count() < 28
					&& !ctx.objects.select().id(oreIds).isEmpty()
					&& ctx.players.local().getAnimation() == -1;
		}

		@Override
		public void execute() {
			
			GameObject ore = ctx.objects.nearest().poll();
			if(ore.isOnScreen()){
				ore.interact("mine");
			} else {
				ctx.movement.stepTowards(ore);
				ctx.camera.turnTo(ore);
			}
			
			
		} 
		
	}
	
	
	public class Drop extends Task{
		private int oreId = 434;
		
		
		public Drop(MethodContext ctx) {
			super(ctx);
			// TODO Auto-generated constructor stub
		}

		@Override
		public boolean active() {
			// TODO Auto-generated method stub
			return ctx.backpack.select().count() == 28;
		
		}

		@Override
		public void execute() {
			for(Item i : ctx.backpack.id(oreId)){
				i.interact("Drop");
				sleep(350, 500);
			}
			
			// TODO Auto-generated method stub
			
		}
		
	}
	
	/**@Override
	public class start(){
		
	} */
	
	
	@Override
	public void repaint(Graphics g) {
		g.setColor(Color.WHITE);
		g.setFont(new Font("Tahoma", Font.BOLD, 16));
		g.drawString("Ores mined: " + ores, 50, 50);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messaged(MessageEvent e) {
		String msg = e.getMessage().toLowerCase();
		if(msg.contains("You got some"))
			ores++;
		
		// TODO Auto-generated method stub
		
	}

	
	public int poll() {
		// TODO Poll class
		for (Task task : taskList){
			if(task.active()){
			task.execute();
			return 500;
			}
				
		}
		return 500;
	}
	
	public abstract class Task extends MethodProvider{

		public Task(MethodContext ctx) {
			super(ctx);
			// TODO Task class
		}
		public abstract boolean active();
		public abstract void execute();
		
	}

}
