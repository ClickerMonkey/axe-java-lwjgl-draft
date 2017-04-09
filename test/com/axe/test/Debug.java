package com.axe.test;

import com.axe.Axe;
import com.axe.game.Game;
import com.axe.game.GameEvent;
import com.axe.monitor.MonitorEvent;
import com.axe.monitor.MonitorSystem;
import com.axe.window.Window;
import com.axe.window.WindowEvent;

public class Debug 
{

	public static void listen(Game game)
	{
		game.on(GameEvent.Start, (g) -> Axe.logger.log("Game Start"));
		game.on(GameEvent.Stop, (g) -> Axe.logger.log("Game Stop"));
	}

	public static void listen(Window window)
	{
		window.on(WindowEvent.Blur, (w) -> Axe.logger.log("%s Window Blur", w.getTitle()));
		window.on(WindowEvent.Focus, (w) -> Axe.logger.log("%s Window Focus", w.getTitle()));
		window.on(WindowEvent.Maximized, (w) -> Axe.logger.log("%s Window Maximized", w.getTitle()));
		window.on(WindowEvent.Minimized, (w) -> Axe.logger.log("%s Window Minimized", w.getTitle()));
		window.on(WindowEvent.Resize, (w, x, y, px, py) -> Axe.logger.log("%s Window Resized (%d, %d)", w.getTitle(), x, y));
		window.on(WindowEvent.Restored, (w) -> Axe.logger.log("%s Window Restored", w.getTitle()));
	}
	
	public static void listen(MonitorSystem monitors)
	{
		monitors.on(MonitorEvent.Connected, (m) -> Axe.logger.log("Monitor Connected %s", m));
		monitors.on(MonitorEvent.Disconnected, (m) -> Axe.logger.log("Monitor Disconnected %s", m));
	}
	
}
