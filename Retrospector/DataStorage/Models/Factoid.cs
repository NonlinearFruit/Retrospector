using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace Retrospector.DataStorage.Models
{
    [Table("Factoids")]
    public class Factoid
    {
        [Key]
        public int Id { get; set; }
        public string Title { get; set; }
        public string Content { get; set; }
        public DateTime CreatedDate { get; set; }
        public int MediaId { get; set; }
        public virtual Media Media { get; set; }
    }
}