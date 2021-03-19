using System;
using System.Windows.Input;

namespace Retrospector.Utilities
{
    public class DelegateCommand : ICommand
    {
        public event EventHandler CanExecuteChanged;

        private readonly Action<object> _execute;
        private readonly Predicate<object> _canExecute;

        public DelegateCommand(Action<object> execute, Predicate<object> canExecute = null)
        {
            _execute = execute;
            _canExecute = canExecute;
        }

        public bool CanExecute(object parameter)
        {
            return _canExecute?.Invoke(parameter) ?? true;
        }

        public void Execute(object parameter)
        {
            _execute.Invoke(parameter);
        }

        public void RaiseCanExecuteChanged()
        {
            if (_canExecute != null)
                CanExecuteChanged?.Invoke(this, EventArgs.Empty);
        }
    }
}